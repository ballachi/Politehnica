/*Bulachi Marin - 311CB*/
/*-- LDG.h --- LISTA DUBLE INLANTUITA GENERICA ---*/
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <stdarg.h>
#include <time.h>

#ifndef _LISTA_GENERICA_
#define _LISTA_GENERICA_




typedef struct celulag
{ 
	void* info;           /* adresa informatie */
	struct celulag *urm,*pre;   /* adresa urmatoarei celule */
}	TCelulaG, *TLG, **ALG; /* tipurile Celula, Lista si Adresa_Lista */

typedef struct 
{
	TLG *vector;
	int M;
} THashMapG, *THMG, **AHMG;

typedef struct 
{ 
	char key[200], ip[20];
	int Frequency;	
} Tsite; // struct


THMG alocaHashMap(size_t M);
void AFI(void * ae);
void AfiElement(void * ae,FILE *hash_out);
void Afisare(ALG aL,FILE *hash_out,int i);
void Afisareindex(ALG aL,FILE *hash_out,int i);
void printHashMap(THMG hash_map,FILE *hash_out);
int InsLDG(ALG aL, void* ae, size_t d);
int set(char *key, char *value, THMG hash_map);
int hash_function(char *key);
char *get(char *key, THMG hash_map);
void removeL(THMG hash_map,char *key);
int compKey(char *key, void *ae); 
int put_hash(THMG hash_map,THMG hash_map2,int M);
void Distruge(ALG aL);
void ordoneaza(ALG aL);
int numara_lista(ALG lista);
void stergeultima(ALG aL);
int num_tot(AHMG hash_map);
void distrugetabelahash(AHMG hash_map);
#endif
