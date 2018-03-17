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

#define BUFLEN 256

void error(char *msg) {
    perror(msg);
    exit(0);
}

// Transmite un mesaj prin TCP
void send_tcp(int socket, char buffer[]) {
    int n = send(socket, buffer, strlen(buffer), 0);
    if (n < 0)
        error("-10 : Eroare la apel socket");
}

int main(int argc, char *argv[]) {
    int sockfd, n, fd, i, j;;
    int conect = 0;
    int fdmax;
    struct sockaddr_in serv_addr;
    struct sockaddr_in server_addr;
    socklen_t addr_size;

    // UDP socket
    fd = socket(PF_INET, SOCK_DGRAM, 0);
    if (fd < 0){
        error("-10 : Eroare la apel socket");
    }

    server_addr.sin_family = AF_INET;
    server_addr.sin_port = htons(atoi(argv[2]));
    inet_aton(argv[1], &server_addr.sin_addr);
    addr_size = sizeof (server_addr);
    memset(server_addr.sin_zero, 0, sizeof (server_addr.sin_zero));



    char buffer[BUFLEN];
    char aux[BUFLEN];
    if (argc < 3) {
        fprintf(stderr, "Usage %s server_address server_port\n", argv[0]);
        exit(0);
    }
    // TCP socket
    sockfd = socket(AF_INET, SOCK_STREAM, 0);
    if (sockfd < 0)
        error("-10 : Eroare la apel socket");

    serv_addr.sin_family = AF_INET;
    serv_addr.sin_port = htons(atoi(argv[2]));
    inet_aton(argv[1], &serv_addr.sin_addr);


    fd_set read_fds; //multimea de citire folosita in select()
    fd_set tmp_fds; //multime folosita temporar 
    FD_ZERO(&read_fds);
    FD_ZERO(&tmp_fds);
    FD_SET(0, &read_fds);
    FD_SET(fd, &read_fds);
    FD_SET(sockfd, &read_fds);
    fdmax = (sockfd > fd) ? sockfd : fd;

    if (connect(sockfd, (struct sockaddr*) &serv_addr, sizeof (serv_addr)) < 0)
        error("-10 : Eroare la apel connecting");


    int id = getpid();
    char nume_fis[BUFLEN];

    sprintf(nume_fis, "client-<%d>.log", id);
    FILE *f = fopen(nume_fis, "w+");
    int numar_card = 0;

    while (1) {
        tmp_fds = read_fds;
        if (select(fdmax + 1, &tmp_fds, NULL, NULL, NULL) == -1)
            error("-10 : Eroare la apel select");

        for (i = 0; i <= fdmax; i++) {
            if (FD_ISSET(i, &tmp_fds)) {

                if (i == 0) {
                    //citesc de la tastatura
                    memset(buffer, 0, BUFLEN);
                    memset(aux, 0, sizeof (aux));

                    fgets(buffer, BUFLEN - 1, stdin);
                    fprintf(f, "%s", buffer);
                    sscanf(buffer, "%s", aux);
// Daca este conectat si scrie login
                    if (conect == 1 && (strcmp(aux, "login") == 0 || strcmp(aux, "unlock") == 0)) {
                        printf("−2 : Sesiune deja deschisa\n");
                        fprintf(f, "−2 : Sesiune deja deschisa\n");
// Daca  nu este conectat si scrie o comanda diferita de login si unlock
                    } else if (conect == 0 && (strcmp(aux, "logout") == 0 || strcmp(aux, "listsold") == 0
                            || strcmp(aux, "getmoney") == 0 || strcmp(aux, "putmoney") == 0)) {
                        printf("−1 : Clientul nu este autentificat\n");
                        fprintf(f, "−1 : Clientul nu este autentificat\n");
                    } else if (strcmp(aux, "quit") == 0) {
                        send_tcp(sockfd, buffer);
                        close(sockfd);
                        close(fd);
                        fclose(f);
                        return 0;
                    } else if (strcmp(aux, "unlock") == 0) {
                        memset(buffer, 0, BUFLEN);
                        sprintf(buffer, "unlock %d", numar_card);
                        // trimit mesajul "unlock <numar card>"
                        sendto(fd, buffer, strlen(buffer), 0, (struct sockaddr *) &server_addr, addr_size);
                        // astept raspuns
                        recvfrom(fd, buffer, BUFLEN, 0, NULL, NULL);

                        printf("%s\n", buffer);
                        fprintf(f, "%s\n", buffer);
                        // daca raspunsul este UNLOCK> Trimite parola secreta
                        if (strcmp(buffer, "UNLOCK> Trimite parola secreta") == 0) {
                            memset(buffer, 0, BUFLEN);
                            memset(aux, 0, BUFLEN);

                            fgets(aux, BUFLEN - 1, stdin);
                            fprintf(f, "%s", aux);
                            sprintf(buffer, "unlock2 %d %s", numar_card, aux);
                            // trimit mesajul "unlock2 <numar card> <parola secreta>"
                            sendto(fd, buffer, strlen(buffer), 0, (struct sockaddr *) &server_addr, addr_size);
                            memset(buffer, 0, BUFLEN);
                            // astept raspuns
                            recvfrom(fd, buffer, BUFLEN, 0, NULL, NULL);
                            printf("%s\n", buffer);
                            fprintf(f, "%s\n", buffer);

                        }

                    } else {
                        if (strcmp(aux, "login") == 0) {
                            // salvez ultimul numar_card
                            sscanf(buffer, "%s%d", aux, &numar_card);
                        }
                        send_tcp(sockfd, buffer);
                    }

                }
                else {
                    // am primit date pe unul din socketii
                    memset(buffer, 0, BUFLEN);
                    if ((n = recv(i, buffer, sizeof (buffer), 0)) <= 0) {
                        if (n == 0) {
                            //conexiunea s-a inchis
                            printf("client: socket %d hung up\n", i);
                        } else {
                            error("-10 : Eroare la apel recv");;
                        }
                        close(i);
                        FD_CLR(i, &read_fds); // scoatem din multimea de citire socketul 
                        close(sockfd);
                        close(fd);
                        fclose(f);
                        return 0;
                    } else {
                        memset(aux, 0, sizeof (aux));
                        sscanf(buffer, "%s%s", aux, aux);
                        if (strcmp(aux, "Welcome") == 0) {
                            conect = 1; // salvez ca clientul este logat
                        } else if (strcmp(aux, "Deconectare") == 0) {
                            conect = 0; 
                        } else if (strcmp(aux, "quit") == 0) {
                            for (j = 0; j <= fdmax; j++) {
                                if (FD_ISSET(j, &read_fds)) {
                                   close(j);
                                   FD_CLR(j, &read_fds); 
                                }
                            }
                            fclose(f);
                            return 0;
                        }
                        printf("%s\n", buffer);
                        fprintf(f, "%s\n", buffer);
                    }
                }
            }
        }
    }
    close(sockfd);
    close(fd);
    fclose(f);
    return 0;
}


