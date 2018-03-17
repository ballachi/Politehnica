#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h> 
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <netinet/in.h>
#include <errno.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <netdb.h> 

#define MAX_CLIENTS 10
#define MAXLEN 500
#define PORT 80
#define MAXNAME 255
#define IP_LEN 32

void error(char *msg) {
    perror(msg);
    exit(1);
}

void getIP(char* name, char ip[IP_LEN]) {
    struct hostent *a;
    a = gethostbyname(name);
    strcpy(ip, (char*) (inet_ntoa(*((struct in_addr *) (a->h_addr_list[0])))));
}

int main(int argc, char *argv[]) {
    int sockfd, newsockfd, portno;
    unsigned int clilen;
    char buffer[MAXLEN];
    struct sockaddr_in serv_addr, cli_addr;
    int n, i, j;

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

    sockfd = socket(AF_INET, SOCK_STREAM, 0);
    if (sockfd < 0)
        error("ERROR opening socket");

    portno = atoi(argv[1]);

    memset((char *) &serv_addr, 0, sizeof (serv_addr));
    serv_addr.sin_family = AF_INET;
    serv_addr.sin_addr.s_addr = INADDR_ANY; // foloseste adresa IP a masinii
    serv_addr.sin_port = htons(portno);

    if (bind(sockfd, (struct sockaddr *) &serv_addr, sizeof (struct sockaddr)) < 0)
        error("ERROR on binding");

    listen(sockfd, MAX_CLIENTS);

    //adaugam noul file descriptor (socketul pe care se asculta conexiuni) in multimea read_fds
    FD_SET(sockfd, &read_fds);
    fdmax = sockfd;
    // main loop
    while (1) {
        tmp_fds = read_fds;
        if (select(fdmax + 1, &tmp_fds, NULL, NULL, NULL) == -1)
            error("ERROR in select");

        for (i = 0; i <= fdmax; i++) {
            if (FD_ISSET(i, &tmp_fds)) {

                if (i == sockfd) {
                    // a venit ceva pe socketul inactiv(cel cu listen) = o noua conexiune
                    // actiunea serverului: accept()
                    clilen = sizeof (cli_addr);
                    if ((newsockfd = accept(sockfd, (struct sockaddr *) &cli_addr, &clilen)) == -1) {
                        error("ERROR in accept");
                    } else {
                        //adaug noul socket intors de accept() la multimea descriptorilor de citire
                        FD_SET(newsockfd, &read_fds);
                        if (newsockfd > fdmax) {
                            fdmax = newsockfd;
                        }
                    }
                } else {
                    // am primit date pe unul din socketii cu care vorbesc cu clientii
                    //actiunea serverului: recv()
                    memset(buffer, 0, MAXLEN);

                    if ((n = recv(i, buffer, sizeof (buffer), 0)) <= 0) {
                        close(i);
                        FD_CLR(i, &read_fds); // scoatem din multimea de citire socketul pe care 
                    } else { //recv intoarce >0
                        char comanda[10];
                        char stringPOST[MAXLEN];
                        memset(stringPOST, 0, MAXLEN);
                        memset(comanda, 0, sizeof (comanda));
                        sscanf(buffer, "%s", comanda);
                        // Verific daca comanda e POST 
                        // salvez in stringPOST token 
                        if (strcmp(comanda, "POST") == 0) {
                            for (j = strlen(buffer); j > 0; j--) {
                                if (buffer[j] == ' ' || buffer[j] == '\n') {
                                    sscanf(buffer + j + 1, "%s", stringPOST);
                                    break;
                                }
                            }
                        }
                        // extrag numele site-ului
                        char site[MAXNAME];
                        memset(site, 0, MAXNAME);
                        sscanf(buffer, "%s%s", comanda, site);
                        j = 0;
                        // daca inaintea site-ului este http mut cursorul pe pozitia 7
                        if (site[0] == 'h' && site[1] == 't' && site[2] == 't' && site[3] == 'p' && site[4] == ':') {
                            j = 7;
                        }
                        int count = 0;
                        char host[MAXNAME];
                        memset(host, 0, MAXNAME);
                        // scriu numele site-ului in host
                        for (; j < strlen(site); j++, count++) {
                            if (site[j] == '/') break;
                            host[count] = site[j];
                        }
                        char ip[IP_LEN];
                        memset(ip, 0, IP_LEN);
                        //obtin ip
                        getIP(host, ip);

// Creez conexiunea cu serverul
                        int sockfd2;
                        struct sockaddr_in servaddr;
                        char sendbuf[MAXLEN];
                        char recvbuf[MAXLEN];

                        if ((sockfd2 = socket(AF_INET, SOCK_STREAM, 0)) < 0) {
                            printf("Eroare la  creare socket.\n");
                            exit(-1);
                        }
                        /* formarea adresei serverului */
                        memset(& servaddr, 0, sizeof (servaddr));
                        servaddr.sin_family = AF_INET;
                        servaddr.sin_port = htons(PORT);

                        if (inet_aton(ip, & servaddr.sin_addr) <= 0) {
                            printf("Adresa IP invalida.\n");
                            exit(-1);
                        }

                        /*  conectare la server  */
                        if (connect(sockfd2, (struct sockaddr *) & servaddr, sizeof (servaddr)) < 0) {
                            printf("Eroare la conectare\n");
                            exit(-1);
                        }
                        memset(sendbuf, 0, sizeof (sendbuf));
// Transmit mesajul
                        strcpy(sendbuf, buffer);
                        strcat(sendbuf, "\r\n\r\n");
                        int k;
                        char name_file[200];
                        memset(name_file, 0, sizeof (name_file));
// creez numele fisierului
                        for (k = 0; k < strlen(site); k++) {
                            if (site[k] < 65) {  // orice caracter mai mic ca 65
                                name_file[k] = '_';
                            } else {
                                name_file[k] = site[k];
                            }
                        }
// adaug token
                        int p;
                        for (p = 0; p < strlen(stringPOST) && p < 200 ; p++) {
                            if (stringPOST[k] < 65) {
                                name_file[k + p] = '_';
                            } else {
                                name_file[k + p] = stringPOST[p];
                            }
                        }
                        FILE *fcache = fopen(name_file, "r");
                        // daca nu exista fisierul
                        if (fcache == NULL) {
                        	// Trimit mesajul print TCP
                            int x = send(sockfd2, sendbuf, sizeof (sendbuf), 0);
                            if (x < 0) {
                                printf("Eroare la send\n");
                            }
                            // Creez fisierul nou
                            FILE *f = fopen(name_file, "w+");
                            if (f == NULL) {
                                printf("Eroare la deschiderea fisierul\n");
                                exit(1);
                            }
                            memset(recvbuf, 0, sizeof (recvbuf));
                            x = 0;
                            // astept raspunsul de la server
                            // scriu in fisier
                            // transmit clientului
                            while ((x = recv(sockfd2, recvbuf, sizeof (recvbuf), 0)) != 0) {
                                fwrite(recvbuf, sizeof(char), x, f);
                                send(i, recvbuf, x, 0);
                                memset(recvbuf, 0, sizeof (recvbuf));
                            }
                            fclose(f);
                        } else {
                            // daca exista fisierul
                            int nr;
                            memset(recvbuf, 0, sizeof (recvbuf));
                            // citesc din fisier
                            // transmit clientului
                            while ((nr = fread(recvbuf, sizeof(char), sizeof (recvbuf), fcache)) > 0) {
                                if (nr <= 0) break;
                                send(i, recvbuf, nr, 0);
                                memset(recvbuf, 0, sizeof (recvbuf));
                            }
                            fclose(fcache);
                        }
                        // inchid conexiunea cu clientul
                        close(i);
                        FD_CLR(i, &read_fds);
                        // inchid conexiunea cu serverul
                        close(sockfd2);

                    }
                }
            }
        }
    }

    close(sockfd);

    return 0;
}
