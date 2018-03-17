#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include "lib.h"

#define HOST "127.0.0.1"
#define PORT 10001
#define FOREVER while(1)
#define MAX_TIME_OUT 3
#define ACK 'Y'
#define NAK 'N'
#define MAX_TIME 5000
#define MODULO 64
#define MAX_LEN_NAME 30


void write_msg(msg *t, char type, char *data, int size_data, int seq);

int main(int argc, char** argv) {
    msg t;
    mini_kermit k;
    Send_Init s_init;
    init(HOST, PORT);
    int seq = 0;
    int f;
    int time_out = 0;
    int time = MAX_TIME;
    FOREVER{
        if (time_out == MAX_TIME_OUT) return 0;
        msg *r = receive_message_timeout(time);
        if (r == NULL) {   //in caz ca nu primeste pachet 
            time_out++;
            perror("send error " );
        } else {
            if(seq==MODULO) seq=0;
            time_out = 0;
            memcpy(&k, r->payload, 4);   // pun in sctructura primele 4 cimpuri
            memcpy(&k.CHECK, r->payload + 4 + k.LEN, 3); //pun in structura cele 2 cimpuri care se afla dupa data
            memset(r->payload + 4 + k.LEN, 0, 3);    //cele 2 cimpuri  dupa data le fac 0
            unsigned short crc16 = crc16_ccitt(r->payload, sizeof (r->payload));
            if (k.CHECK == crc16 &&  seq == k.SEQ ) {   //in caz ca mesaju nu este eronat
                k.DATA = calloc(k.LEN, sizeof (char));  
                memcpy(k.DATA, r->payload + 4, k.LEN); //scriu in k.data data din payload

                if (k.TYPE == 'B') {    // daca este de tip "b"
                    write_msg(&t, ACK, NULL, 0, seq);  //scriu in t.payload un pachet de tip "Y"
                    break;
                } else if (k.TYPE == 'S') {

                    k.DATA = calloc(k.LEN, sizeof (char));
                    memcpy(k.DATA, r->payload + 4, k.LEN); 
                    memcpy(&s_init, k.DATA, k.LEN); // scriu in structura Send init
                    time = s_init.TIME * 1000;      //setez timpu (il transform din secunde in milisecunde)
                    write_msg(&t, ACK, k.DATA, k.LEN, seq);  ////scriu in t.payload un pachet de tip "Y"

                } else if (k.TYPE == 'F') {
                    char name_file[MAX_LEN_NAME] = "recv_"; 
                    strcat(name_file, k.DATA);  //numele fisierului in care voi scrie data
                    f = open(name_file, O_WRONLY | O_CREAT, 0777);  //deschid fisierul
                    write_msg(&t, ACK, NULL, 0, seq);   

                } else if (k.TYPE == 'D') {
                    write(f, k.DATA, k.LEN);  //scriu in fisier 
                    write_msg(&t, ACK, NULL, 0, seq);
                } else if (k.TYPE == 'Z') {
                    write_msg(&t, ACK, NULL, 0, seq); 
                    close(f);
                }
                seq++;
                free(k.DATA);
            } else {
                write_msg(&t, NAK, NULL, 0, seq);   //scriu in t.payload un pachet de tip "N"
                seq++;
            }

            t.len = strlen(t.payload);
            send_message(&t);

        }
    }
    t.len = strlen(t.payload);
    send_message(&t);



    return 0;
}


//functia scrie orice pachet in payload si actualizeaza t.len
void write_msg(msg *t, char type, char *data, int size_data, int seq) {

    mini_kermit k;
    memset(&k, 0, sizeof (mini_kermit));  //initializez cu 0
    memset(t, 0, sizeof (msg));
    k.SOH = 0X01;
    k.LEN = size_data;
    k.SEQ = seq;
    k.TYPE = type;
    k.DATA = calloc(k.LEN, 1);

    memcpy(k.DATA, data, k.LEN);   // scriu in k.data data
    memcpy(t->payload, &k, 4);      // scriu primii 4 octeti
    memcpy(t->payload + 4, k.DATA, k.LEN);  //scriu data in payload

    k.CHECK = crc16_ccitt(t->payload, sizeof (t->payload));
    k.MARK = 0x0D;
    memcpy(t->payload + 4 + k.LEN, &k.CHECK, 3);
    t->len = sizeof (t);
    free(k.DATA);       //eliberez memoaria
}