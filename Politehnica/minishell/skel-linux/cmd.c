/**
 * Operating Systems 2013-2017 - Assignment 2
 *
 * Marin Bulachi 331CB
 *
 */

#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#include <sys/types.h>
#include <sys/stat.h>
#include <sys/wait.h>

#include <fcntl.h>
#include <unistd.h>

#include "cmd.h"
#include "utils.h"


#define READ		0
#define WRITE		1

/**
 * Internal change-directory command.
 */
static bool shell_cd(word_t *dir)
{
	/* execute cd */
	//DIE(dir == NULL, "shell_cd: dir este NULL");
	if (dir == NULL)
		return false;
	char *director = get_word(dir);
	int val = chdir(director);

	free(director);
	return val;
}

/**
 * Internal exit/quit command.
 */
static int shell_exit(void)
{
	/* execute exit/quit */

	return SHELL_EXIT;
}

/**
 * Redirectare
 */
static void redirect(word_t *in, word_t *out, word_t *err, int io_flags)
{
	char *file;
	int fd;
	int mode = 0;

	if (in != NULL) {
		file = get_word(in);
		// deschid fisierul
		fd = open(file, O_RDONLY, 0644);
		DIE(fd < 0, "open");
		// redirectez inputul din fisier
		DIE(dup2(fd, STDIN_FILENO), "dup2");
		DIE(close(fd) < 0, "close");
		free(file);

	}
	if (io_flags == 0)
		mode = O_WRONLY | O_CREAT | O_TRUNC;
	else
		mode = O_WRONLY | O_CREAT | O_APPEND;

	if (out != NULL) {
		// redirectez outputul in fisier
		file = get_word(out);
		fd = open(file, mode, 0644);
		DIE(fd < 0, "open");
		DIE(dup2(fd, STDOUT_FILENO) < 0, "dup2");
		if (err == NULL) {
			DIE(close(fd) < 0, "close");
			free(file);
		}
	}
	if (err != NULL) {
		// redirectez eroarea in fisier
		char *file2 = get_word(err);
		// daca out este diferit de err
		if (out == NULL || strcmp(file2, file) != 0) {
			DIE(close(fd) < 0, "close");
			fd = open(file2, mode, 0644);
			DIE(fd < 0, "open");
		}
		DIE(dup2(fd, STDERR_FILENO) < 0, "dup2");
		DIE(close(fd) < 0, "close");
		free(file2);
		if (out != NULL)
			free(file);

	}
}


/**
 * Parse a simple command (internal, environment variable assignment,
 * external command).
 */
static int parse_simple(simple_command_t *s, int level, command_t *father)
{


	char *command;
	pid_t pid;
	int status, i, size;
	char **argv = NULL;
	int val;


	/* TODO sanity checks */
	if (s == NULL)
		return -1;

	/* TODO if variable assignment, execute the assignment and return
	 * the exit status
	 */

	if (s->verb->next_part != NULL) {
		argv = get_argv(s, &size);
		val = putenv(argv[0]);
		return val;
	}


	command = get_word(s->verb);

	if (!strcmp(command, "exit") || !strcmp(command, "quit")) {
		free(command);
		return shell_exit();
	}

	if (!strcmp(command, "cd")) {
		if (s->out != NULL) {
			char *cd = get_word(s->out);
			int fd = open(cd, O_WRONLY | O_CREAT | O_TRUNC, 0644);

			free(cd);
			DIE(close(fd) < 0, "close");
		}

		free(command);
		return shell_cd(s->params);
	}

	//  TODO if external command:
	//  *   1. fork new process
	//  *     2c. perform redirections in child
	//  *     3c. load executable in child
	//  *   2. wait for child
	//  *   3. return exit status
	size = 0;
	argv = get_argv(s, &size);
	if (argv == NULL)
		return EXIT_FAILURE;

	pid = fork();
	switch (pid) {
	case -1:
		/* error forking */
		return EXIT_FAILURE;
	case 0:
		/* child process */
		redirect(s->in, s->out, s->err, s->io_flags);
		execvp(command, argv);

		/* only if exec failed */
		fprintf(stderr, "Execution failed for '%s'\n", command);
		exit(EXIT_FAILURE);
	default:
		/* parent process */
		break;
	}

	/* only parent process gets here */
	waitpid(pid, &status, 0);
	// eliberez memoria
	for (i = 0; i < size; i++)
		free(argv[i]);

	free(argv);
	free(command);
	return status;


}

/**
 * Process two commands in parallel, by creating two children.
 */
static bool do_in_parallel(command_t *cmd1, command_t *cmd2, int level,
		command_t *father)
{
	/* execute cmd1 and cmd2 simultaneously */

	pid_t pid;
	int status;

	pid = fork();
	switch (pid) {
	case -1:
		/* error forking */
		return EXIT_FAILURE;
	case 0:
		/* child process */
		parse_command(cmd2, level+1, father);
		/* only if exec failed */
		exit(EXIT_FAILURE);
	default:
		/* parent process */
		parse_command(cmd1, level+1, father);
		break;
	}

	/* only parent process gets here */
	waitpid(pid, &status, 0);

	return status;

}

/**
 * Run commands by creating an anonymous pipe (cmd1 | cmd2)
 */
static bool do_on_pipe(command_t *cmd1, command_t *cmd2, int level,
		command_t *father)
{

	/* redirect the output of cmd1 to the input of cmd2 */

	pid_t pid;
	int status;
	int filedes[2];
	int val;
	int in;


	level++;
	in = dup(STDIN_FILENO);
	DIE(in < 0, "dup");
	pipe(filedes);

	pid = fork();
	switch (pid) {
	case -1:
		/* error forking */
		return EXIT_FAILURE;
	case 0:
		/* child process */

		DIE(close(filedes[READ]) < 0, "close");
		DIE(dup2(filedes[WRITE], STDOUT_FILENO) < 0, "dup2");
		DIE(close(filedes[WRITE]) < 0, "close");
		parse_command(cmd1, level, father);
		/* only if exec failed */
		exit(EXIT_FAILURE);

	default:
		/* parent process */
		DIE(close(filedes[WRITE]) < 0, "close");
		DIE(dup2(filedes[READ], STDIN_FILENO) < 0, "dup2");
		DIE(close(filedes[READ]) < 0, "close");
		val = parse_command(cmd2, level, father);
		break;
	}
	/* only parent process gets here */
	waitpid(pid, &status, 0);
	DIE(dup2(in, STDIN_FILENO) < 0, "dup2");
	DIE(close(in) < 0, "close");

	return val;
}


/**
 * Parse and execute a command.
 */
int parse_command(command_t *c, int level, command_t *father)
{
	/* sanity checks */
	if (c == NULL)
		return EXIT_FAILURE;

	level++;
	if (c->op == OP_NONE) {
		/* execute a simple command */
		return parse_simple(c->scmd, level, father);
	}

	switch (c->op) {
	case OP_SEQUENTIAL:
		/* execute the commands one after the other */
		parse_command(c->cmd1, level, c);
		return parse_command(c->cmd2, level, c);

	case OP_PARALLEL:
		/* execute the commands simultaneously */
		return do_in_parallel(c->cmd1, c->cmd2, level, c);

	case OP_CONDITIONAL_NZERO:
		/* execute the second command only if the first one
		 * returns non zero
		 */
		if (parse_command(c->cmd1, level, c) != 0)
			return parse_command(c->cmd2, level, c);
		return 0;

	case OP_CONDITIONAL_ZERO:
		/* execute the second command only if the first one
		 * returns zero
		 */
		if (parse_command(c->cmd1, level, c) == 0)
			return parse_command(c->cmd2, level, c);
		return 1;

	case OP_PIPE:
		/* TODO redirect the output of the first command to the
		 * input of the second
		 */
		return do_on_pipe(c->cmd1, c->cmd2, level+1, c);

	default:
		return SHELL_EXIT;
	}

	return EXIT_FAILURE; /* TODO replace with actual exit code of command */
}
