#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h> 
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <netdb.h> 

#define MAX_CLIENTS 5
#define BUFLEN 256
#define SIZE_NAME 12
#define SIZE_PAROLA_SECRETA 16

struct sockaddr_storage server_storage;
char buffer[BUFLEN];

void error(char *msg) {
	perror(msg);
	exit(1);
}

int N;

//structura Client pentru a pastra datele
struct client {
	char nume[SIZE_NAME];
	char prenume[SIZE_NAME];
	unsigned int numar_card;
	unsigned int pin;
	char parola_secret[SIZE_PAROLA_SECRETA];
	double sold;
	int blocat;
	int socket;  // pe ce socket este conectat clientul
	int pin_gresit;

} typedef Client;

// Citirea din fisier
void read_file(Client c[], FILE *f) {
	int i;
	for (i = 0; i < N; i++) {
		memset(&c[i], 0, sizeof (c[i]));
		fscanf(f, "%s", c[i].nume);
		fscanf(f, "%s", c[i].prenume);
		fscanf(f, "%u", &c[i].numar_card);
		fscanf(f, "%u", &c[i].pin);
		fscanf(f, "%s", c[i].parola_secret);
		fscanf(f, "%lF", &c[i].sold);
		c[i].socket = -1;
		c[i].blocat = 0;
	}
}

// Transmite un mesaj prin TCP
void send_tcp(int socket, char buffer[]){
	int n = send(socket, buffer, strlen(buffer), 0);
	if (n < 0)
		error("-10 : Eroare la apel socket");
}

// Transmite un mesaj prin UDP
void send_udp(int socket, char buffer[]){
	int n= sendto(socket, buffer, strlen(buffer), 0,
		(struct sockaddr *) &server_storage, sizeof (server_storage));
	if (n < 0) 
        error("-10 : Eroare la apel socket");
}

void cod_eroare(int i, int socket) {
	memset(buffer, 0, BUFLEN);
	switch (i) {
		case -2:
			sprintf(buffer, "ATM> -2 : Sesiune deja deschisa");
			send_tcp(socket,buffer);
			break;

		case -3:
			sprintf(buffer, "ATM> -3 : Pin gresit");
			send_tcp(socket,buffer);
			break;
		case -4:
			sprintf(buffer, "ATM> -4 : Numar card inexistent");
			send_tcp(socket,buffer);
			 break;

		case -5:
			sprintf(buffer, "ATM> -5 : Card blocat");
			send_tcp(socket,buffer);
			 break;
		case -8:
			sprintf(buffer, "ATM> −8 : Fonduri insuficiente");
			send_tcp(socket,buffer);
			 break;
		case -9:
			sprintf(buffer, "ATM> −9 : Suma nu este multiplu de 10");
			send_tcp(socket,buffer);
			break;
		default:
			printf("ATM> -6 : Operatie esuata");
	}
}

/*
Functia login primeste ca parametru clientii, numar_card si
socketul pe care se face conexiunea
Caut in toti clientii numarul cardului
Apoi verific daca nu este blocat
Apoi verific daca nu este cineva conectat la acest card
Apoi verific pinul
*/
void login(Client c[], int numar_card, int pin, int socket) {
	int i;
	for (i = 0; i < N; i++) {
		if (c[i].numar_card == numar_card) {
			if (c[i].blocat == 0) {
				if (c[i].socket == -1) {
					if (c[i].pin == pin) {
						c[i].socket = socket;
						c[i].pin_gresit = 0;
						c[i].blocat = 0;
						char buffer[100];
						sprintf(buffer, "ATM> Welcome %s %s", c[i].nume, c[i].prenume);
						send_tcp(socket,buffer);
						return;
					} else {
						c[i].pin_gresit++;
						if (c[i].pin_gresit > 2) {
							c[i].blocat = 1;
							cod_eroare(-5, socket);
							return;
						} else {
							cod_eroare(-3, socket);
							return;
						}
					}
				} else {
					cod_eroare(-2, socket);
					return;
				}
			} else {
				cod_eroare(-5, socket);
				return;
			}
		}
	}
	cod_eroare(-4, socket);
}

/*
Functia logout primeste Clientii si socketul pe care se face conexiunea
Se verifica la ce client este conectat socketul
Apoi urmeaza deconectare de la bancomat
*/
void logout(Client c[], int socket) {
	int i;
	for (i = 0; i < N; i++) {
		if (c[i].socket == socket) {
			c[i].socket = -1;
			memset(buffer, 0, BUFLEN);
			sprintf(buffer, "ATM> Deconectare de la bancomat");
			send_tcp(socket,buffer);
			return;
		}
	}
}

/*
Functia listsold primeste Clientii si socketul pe care se face conexiunea
Se verifica la ce client este conectat socketul
Apoi se transmite un mesaj catre client cu sold-ul sau
*/
void listsold(Client c[], int socket) {
	int i;
	for (i = 0; i < N; i++) {
		if (c[i].socket == socket) {
			memset(buffer, 0, BUFLEN);
			sprintf(buffer, "ATM> %.2lf", c[i].sold);
			send_tcp(socket,buffer);
			return;
		}
	}
}

/*
Functia getmoney primeste Clientii, suma si socketul pe care se face conexiunea
Se verifica la ce client este conectat socketul
Apoi se verifica daca suma este multiplu de 10
Apoi se verifica daca suma care se doreste nu depaseste suma detinuta de client
Se scade suma si se transmite raspuns
*/
void getmoney(Client c[], unsigned long long suma, int socket) {
	int i;
	for (i = 0; i < N; i++) {
		if (c[i].socket == socket) {
			if (suma % 10 == 0) {
				if (c[i].sold >= suma) {
					c[i].sold -= suma;
					memset(buffer, 0, BUFLEN);
					sprintf(buffer, "ATM> Suma %llu retrasa cu succes", suma);
					send_tcp(socket,buffer);
					return;
				} else {
					cod_eroare(-8, socket);
				}
			} else {
				cod_eroare(-9, socket);
			}
		}
	}
}

/*
Functia putmoney primeste Clientii, suma si socketul pe care se face conexiunea
Se verifica la ce client este conectat socketul
Se adauga suma si se transmite raspuns
*/
void putmoney(Client c[], double suma, int socket) {
	int i;
	for (i = 0; i < N; i++) {
		if (c[i].socket == socket) {
			c[i].sold += suma;
			memset(buffer, 0, BUFLEN);
			sprintf(buffer, "ATM> Suma depusa cu succes");
			send_tcp(socket,buffer);
			return;
		}
	}
}

/*
Functia unlock primeste Clientii , numarul cardului si
socketul pe care se face conexiunea
Verifica daca numarul cardului == cu numar de card din toti clientii
Verifica daca cardul e blocat
Daca nu e blocat transmit un mesaj si astept raspuns 
*/
void unlock(Client c[], int numar_card, int socket) {
	int i;
	for (i = 0; i < N; i++) {
		if (c[i].numar_card == numar_card) {
			memset(buffer, 0, BUFLEN);
			if (c[i].blocat != 1) {
				sprintf(buffer, "UNLOCK> −6 : Operatie esuata");
				send_udp(socket, buffer);
			} else {
				sprintf(buffer, "UNLOCK> Trimite parola secreta");
				send_udp(socket, buffer);
			}
			return;
		}
	}
	memset(buffer, 0, BUFLEN);
	sprintf(buffer, "UNLOCK>  -4 : Numar card inexistent");
	send_udp(socket, buffer);
}

/*
Functia unlock2 primeste Clientii , numarul cardului. parola_secret si
socketul pe care se face conexiunea 
Verifica daca numarul cardului == cu numar de card din toti clientii
verif daca parola corespunde 
daca corespunde pin_gresit va fi setat pe zero si se va transmite mesaj
daca nu corespunde intorc eroare -7
*/
void unlock2(Client c[], int numar_card, char parola_secret[], int socket) {
	int i;
	for (i = 0; i < N; i++) {
		if (c[i].numar_card == numar_card) {
			if (strcmp(c[i].parola_secret, parola_secret) == 0) {
				c[i].pin_gresit = 0;
				c[i].blocat = 0;
				sprintf(buffer, "UNLOCK> Client deblocat");
			} else {
				sprintf(buffer, "UNLOCK> −7 : Deblocare esuata");
			}
			send_udp(socket, buffer);
		}
	}
}


int main(int argc, char *argv[]) {

// Citirea din fisier
	FILE *f = fopen(argv[2], "r+");
	if (f == NULL) {
		printf("fisier inexistent\n");
	}
	fscanf(f, "%d", &N);
	Client c[N];
	read_file(c, f);
	fclose(f);

	int i, fd, portno = atoi(argv[1]);

	struct sockaddr_in server_addr;
	socklen_t addr_size ,clilen;
	// UDP socket
	fd = socket(PF_INET, SOCK_DGRAM, 0);
	if (setsockopt(fd, SOL_SOCKET, SO_REUSEADDR, &(int){ 1 }, sizeof(int)) < 0)
    	error("-10 : Eroare la apel socket");

	server_addr.sin_family = AF_INET;
	server_addr.sin_port = htons(portno);
	server_addr.sin_addr.s_addr = INADDR_ANY; 
	memset(server_addr.sin_zero, 0, sizeof(server_addr.sin_zero));

	int sockfd, newsockfd;
	struct sockaddr_in serv_addr, cli_addr;
	int n;

	fd_set read_fds; //multimea de citire folosita in select()
	fd_set tmp_fds; //multime folosita temporar 
	int fdmax; //valoare maxima file descriptor din multimea read_fds

	if (argc < 2) {
		fprintf(stderr, "Usage : %s port\n", argv[0]);
		exit(1);
	}

	//golim multimea de descriptori de citire (read_fds) si multimea tmp_fds 
	FD_ZERO(&read_fds);
	FD_ZERO(&tmp_fds);

	//TCP socket
	sockfd = socket(AF_INET, SOCK_STREAM, 0);
	if (setsockopt(sockfd, SOL_SOCKET, SO_REUSEADDR, &(int){ 1 }, sizeof(int)) < 0)
    	error("-10 : Eroare la apel socket");

	memset((char *) &serv_addr, 0, sizeof (serv_addr));
	serv_addr.sin_family = AF_INET;
	serv_addr.sin_addr.s_addr = INADDR_ANY; // foloseste adresa IP a masinii
	serv_addr.sin_port = htons(portno);

	if (bind(sockfd, (struct sockaddr *) &serv_addr, sizeof (struct sockaddr)) < 0)
		error("-10 : Eroare la apel binding TCP");


	if (bind(fd, (struct sockaddr *) &server_addr, sizeof (server_addr)) < 0)
		error("-10 : Eroare la apel binding UDP");

	addr_size = sizeof (server_storage);

	listen(sockfd, MAX_CLIENTS);

	//adaugam noul file descriptor (socketul pe care se asculta conexiuni) in multimea read_fds
	FD_SET(0, &read_fds);
	FD_SET(sockfd, &read_fds);
	FD_SET(fd, &read_fds);
	//Aleg maximul dintre udp socket si tcp socket
	fdmax = (sockfd > fd) ? sockfd : fd;

	// main loop
	while (1) {
		tmp_fds = read_fds;
		if (select(fdmax + 1, &tmp_fds, NULL, NULL, NULL) == -1)
			error("-10 : Eroare la apel select");

		for (i = 0; i <= fdmax; i++) {
			if (FD_ISSET(i, &tmp_fds)) {
				if (i == sockfd) {
					// a venit ceva pe socketul inactiv(cel cu listen) = o noua conexiune
					// actiunea serverului: accept()
					clilen = sizeof (cli_addr);
					if ((newsockfd = accept(sockfd, (struct sockaddr *) &cli_addr, &clilen)) == -1) {
						error("-10 : Eroare la apel accept");
					}
					else {
						//adaug noul socket intors de accept() la multimea descriptorilor de citire
						FD_SET(newsockfd, &read_fds);
						if (newsockfd > fdmax) {
							fdmax = newsockfd;
						}
					}
				// a venit ceva pe socketul 0 (stdin)
				} else if (i == 0) {
					memset(buffer, 0, BUFLEN);
					fgets(buffer, BUFLEN - 1, stdin);
					if (strcmp(buffer, "quit\n") == 0) {
						int j;
						for (j = 0; j <= fdmax; j++) {
// transmit un mesaj cu quit pt fiecare client si inchid socketul
// scoatem din multimea de citire socketul 
							if (FD_ISSET(j, &read_fds)) {
								if ( j != 0 && j != fd && j != sockfd) {
									send_tcp(j,buffer);
								}
								close(j);
								FD_CLR(j, &read_fds);
							}
						}
						return 0;
					}
				// daca a venit un mesaj prin socketul udp
				} else if (i == fd) {
					memset(buffer, 0, BUFLEN);
					recvfrom(fd, buffer, BUFLEN, 0, (struct sockaddr *) &server_storage, &addr_size);
					char aux[BUFLEN];
					memset(aux, 0, sizeof (aux));
					sscanf(buffer, "%s", aux);
					int numar_card ;
					if (strcmp(aux, "unlock") == 0) {
						sscanf(buffer, "%s%d", aux, &numar_card);
						unlock(c, numar_card, i);
					}
					else if(strcmp(aux, "unlock2") == 0){
						char parola_secret[SIZE_PAROLA_SECRETA];
						sscanf(buffer, "%s%d%s",aux, &numar_card, parola_secret);
						unlock2(c, numar_card, parola_secret, i);
					}
				// daca a venit un mesaj prin socketul tcp
				} else {

					// am primit date pe unul din socketii cu care vorbesc cu clientii
					//actiunea serverului: recv()
					memset(buffer, 0, BUFLEN);
					if ((n = recv(i, buffer, sizeof (buffer), 0)) <= 0) {
						if (n == 0) {
							//conexiunea s-a inchis
							if (c[i].socket != -1) {  // se verifica daca nu este conectat
								logout(c, i);
							}

						} else {
							error("-10 : Eroare la apel recv");
						}
						close(i);
						FD_CLR(i, &read_fds); // scoatem din multimea de citire socketul
					} else { //recv intoarce > 0
// Se va verifica primul cuvint din mesaj si se va apela functia respectiva
// Pt fiecare functie se va extrage parametrii necesari cu sscanf din buffer
						char aux[BUFLEN];
						memset(aux, 0, sizeof (aux));

						sscanf(buffer, "%s", aux);
						if (strcmp(aux, "login") == 0) {
							int numar_card = 0, pin = 0;
							sscanf(buffer, "%s%d%d", aux, &numar_card, &pin);
							login(c, numar_card, pin, i);


						} else if (strcmp(aux, "logout") == 0) {
							logout(c, i);

						} else if (strcmp(aux, "listsold") == 0) {
							listsold(c, i);

						} else if (strcmp(aux, "getmoney") == 0) {
							unsigned long long suma;
							sscanf(buffer, "%s%llu", aux, &suma);
							getmoney(c, suma, i);

						} else if (strcmp(aux, "putmoney") == 0) {
							double suma;
							sscanf(buffer, "%s%lf", aux, &suma);
							putmoney(c, suma, i);

						} else if (strcmp(aux, "quit") == 0) {
							if (c[i].socket != -1) {  // se verifica daca nu este conectat
								logout(c, i);
							}
							close(i);
							FD_CLR(i, &read_fds); // scoatem din multimea de citire socketul

						}

					}
				}
			}
		}
	}

	close(sockfd);
	close(fd);

	return 0;
}



