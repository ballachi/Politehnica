#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/types.h>
#include <sys/stat.h>
#include "lib.h"

#define HOST "127.0.0.1"
#define PORT 10000
#define MAX_TIME_OUT 3
#define FOREVER while(1)
#define MAX_LEN 250
#define MAX_TIME 5
#define EOL_CHARACTER 0x0D
#define MODULO 64
#define ACK 'Y'
#define NAK 'N'


void write_send_init(msg *t ,int seq);
void write_msg(msg *t, char type, char *data, int size_data, int seq);

int main(int argc, char** argv) {
    msg t;
    init(HOST, PORT);

    int nr_file = 0;
    int time_out = 0;
    int seq = 0; 
    int i;
    for (i = 1; argv[i] != NULL; i++) { //numaru de fisiere
        nr_file++;
    }


    FOREVER{   //pina cind se treansmite packetu Send_init
        if ( time_out == MAX_TIME_OUT ) return 0; // daca sau perdut 3 pachete

        write_send_init( &t , seq);      // scriu Send_init in mini-kermit.DATA si scriu  mini-kermit in payload
        send_message( &t );

        msg *y = receive_message_timeout( 5000 );
        if (y == NULL) {
            time_out++; 
            perror("receive error");
        } else {
            seq++;
            time_out = 0;
            if (y->payload[3] == NAK) {  //daca pachetu e de tip NAC
            } else {                     //nu mai verif daca ACK din cauza ca ne garanteaza ca comunicarea 
               break;                                                                             // este sigura
            }
        }
    }
 
    time_out = 0;
    for (int i = 1; i <= nr_file; i++) {

        if (time_out == MAX_TIME_OUT)  return 0;    // daca sau perdut 3 pachete

        int f = open(argv[i], O_RDONLY, 0777);    //deschid fisierul 
        if (f == -1) {
            printf("Fisierul nu exista!!!\n");    //verific
        } else {

            write_msg(&t, 'F', argv[i], strlen(argv[i]) + 1, seq);  //scriu in payload pachetul "F"
            send_message(&t);                               

            msg *y = receive_message_timeout(5000);
            if (y == NULL) {
                time_out++;
                i--;        // in caz ca sa perdut pachetul mai transmit odata fisierul
                perror("receive error");
            } else {
                seq++;
                if(seq == MODULO) seq=0;
                time_out = 0;
                if (y->payload[3] == NAK) {
                    i--;// in caz ca sa perdut pachetul mai transmit odata fisierul
                } else {
                    int nr_b = 0;
                    char aux[MAX_LEN];
                    memset(aux,0 ,MAX_LEN); //intializez cu zero 
                    time_out = 0;
                    while ((nr_b = read(f, aux, MAX_LEN)) > 0) {

                        time_out = 0;
                        FOREVER{ //pina cind se transmite sau se pierde 3 pachete
                            write_msg(&t, 'D', aux, nr_b, seq);
                            if (time_out == MAX_TIME_OUT) return 0;
                            send_message(&t);
                            msg *y = receive_message_timeout(5000);
                            if (y == NULL) {
                                time_out++;
                                perror("receive error");
                            } else {
                                seq++;
                                if(seq == MODULO) seq=0;
                                time_out = 0;
                                if (y->payload[3] == NAK) {
                                } else {
                                    break;
                                }
                            }
                        }
                        memset(aux,0 ,MAX_LEN);
                    }
                    if (nr_b == 0) { //dupa ceam citit tot fisierul 
                        FOREVER{
                            write_msg(&t, 'Z', NULL, 0, seq);   //transmit pachetul "Z"
                            send_message(&t);
                            msg *y = receive_message_timeout(5000);
                            if (y == NULL) {
                                time_out++;
                            } else {
                                seq++;
                                if(seq == MODULO) seq=0;
                                time_out = 0;
                                if (y->payload[3] == NAK) {
                                } else {
                                    close(f);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

    }
    //ultimul pachet
    time_out = 0;
    FOREVER{
        write_msg(&t, 'B', NULL, 0, seq);
        if (time_out == MAX_TIME_OUT) break;
        send_message(&t);
        msg *y = receive_message_timeout(5000);
        if (y == NULL) {
            time_out++;
            perror("receive error");
        } else {
            seq++;
            if(seq == MODULO) seq=0;
            time_out = 0;
            if (y->payload[3] == NAK) {
            } else {
                break;
            }
        }
    }

    return 0;
}


//functia in care scriu Send_init intrun buffer
void write_send_init(msg *t ,int seq) {

    Send_Init s_init;
    char buf[ sizeof(Send_Init) ];
    memset(&s_init, 0, sizeof (Send_Init)); //initializez cu 0

    s_init.MAXL = MAX_LEN; 
    s_init.TIME = MAX_TIME;
    s_init.EOL = EOL_CHARACTER;

    memcpy(buf, &s_init, sizeof (Send_Init));  //scriu in buf Send_init
    write_msg(t, 'S', buf, sizeof (Send_Init), seq);

}

//functia scrie orice pachet in payload si actualizeaza t.len
void write_msg(msg *t, char type, char *data, int size_data, int seq) {

    mini_kermit k;
    memset(&k, 0, sizeof (mini_kermit)); //initializez cu 0
    memset(t, 0, sizeof (msg));         //initializez cu 0

    k.SOH = 0X01;
    k.LEN = size_data;
    k.SEQ = seq;
    k.TYPE = type;
    k.DATA = calloc(k.LEN, 1);  

    memcpy(k.DATA, data, k.LEN);  // scriu in k.data data
    memcpy(t->payload, &k, 4);    // scriu primii 4 octeti
    memcpy(t->payload + 4, k.DATA, k.LEN);  //scriu data in payload

    k.CHECK = crc16_ccitt(t->payload, sizeof (t->payload));
    k.MARK = EOL_CHARACTER;

    memcpy(t->payload + 4 + k.LEN, &k.CHECK, 3);
    t->len = sizeof (t);

    free(k.DATA);  //eliberez memoaria
}
