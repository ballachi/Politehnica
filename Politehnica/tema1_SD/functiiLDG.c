/*Bulachi Marin - 311CB*/
#include "LDG.h"



THMG alocaHashMap(size_t M){				//functia pentru alocare a tabelului hash
	THMG hash_map = NULL;
	int i;
	hash_map = malloc(sizeof(THashMapG));			
	hash_map->M = M;                      
	hash_map->vector = calloc(M,sizeof(TLG));	//initializam listele cu NULL
  
	return hash_map;
}


void AfiElement(void * ae,FILE *hash_out){		//afisare celula

	Tsite p = *(Tsite*)ae;
	fprintf(hash_out," (%s)",p.ip);
	
}

void Afisare(ALG aL,FILE *hash_out,int i){ 		//afisarea listei
	if(!*aL) return;

	TLG ultimu=(*aL)->pre;
	fprintf(hash_out,"%d:",i);
	for(; (*aL)!=ultimu; aL = &(*aL)->urm){			
		AfiElement((*aL)->info,hash_out);
	}
	AfiElement((*aL)->info,hash_out);
	fprintf(hash_out,"\n");
}



void Afisareindex(ALG aL,FILE *hash_out,int i){ 		//afisarea listei cu indexul i
	if(!*aL){
		fprintf(hash_out,"%d: VIDA\n",i);
		return;
	}

		TLG ultimu=(*aL)->pre;
		fprintf(hash_out,"%d:",i);
	for(; (*aL)!=ultimu; aL = &(*aL)->urm){			
		AfiElement((*aL)->info,hash_out);
	}
	AfiElement((*aL)->info,hash_out);
	fprintf(hash_out,"\n");
}


void printHashMap(THMG hash_map,FILE *hash_out){		//afisarea tebelului hash

	int i;
	for (i = 0; i < hash_map->M; i++) {
	  	Afisare(&hash_map->vector[i],hash_out,i); 
	}
}


int hash_function(char *key){					//hash_function care  calculeaza suma caracterelor 
	char *p=key;
	int nr=strlen(key);
	int i, suma=0;
	for (i=0;i<nr;i++){
		suma+=*p;
		p++;
	}

	return suma;
}

int set(char *key, char *value, THMG hash_map){				//SET

	int hash_code = hash_function(key);				//calculez suma caracterelor
	int nr;
	int bucket_nr = hash_code % hash_map->M;			//calculez backend-ul
	TLG L=hash_map->vector[bucket_nr];				
	nr=numara_lista(&hash_map->vector[bucket_nr]);			//numar elementele din lista
	
	if ( nr >= hash_map->M ){					//daca numar elementele din lista >= M
		stergeultima(&hash_map->vector[bucket_nr]);			//stergem ultimu element
	}
	
	if (L!=NULL){
		TLG ultimu=L->pre;  
		for(; L!=ultimu; L = L->urm){
			if(compKey(key,L->info)==0) return 0;		//parcurgem lista daca gasim elementul return 0;
  		}
		if(compKey(key,L->info)==0) return 0;
	}
	Tsite *a =malloc(sizeof(Tsite));
	strcpy(a->key ,key);					//copiem informatia in "a"
	strcpy(a->ip ,value);
	a->Frequency=0;					
	InsLDG(&hash_map->vector[bucket_nr],a,sizeof(Tsite));	//inserem o celula cu informatia "a" la sfirsit de lista
	ordoneaza(&hash_map->vector[bucket_nr]);		//ordonam lista
	free(a);	
	return 0;
}


char *get(char *key, THMG hash_map){

	int hash_cod = hash_function(key);    			//calculez suma caracterelor
	int bucket_nr = hash_cod % hash_map->M;			//calculez backend-ul
	TLG L=hash_map->vector[bucket_nr];
	if (L==NULL) return NULL;

	TLG ultimu=L->pre;
	for(; L!=ultimu; L = L->urm){				//parcurg lista
		if(compKey(key,L->info)==0){
			Tsite *a =malloc(sizeof(Tsite));
			memcpy(a,L->info,sizeof(Tsite));	
			a->Frequency++;				
			memcpy(L->info,a,sizeof(Tsite));
			ordoneaza(&hash_map->vector[bucket_nr]);	//ordonez lista
			free(a);  
			return a->ip;	
		}
	}

	if(compKey(key,L->info)==0){
		Tsite *a =malloc(sizeof(Tsite));
		memcpy(a,L->info,sizeof(Tsite));
		a->Frequency++;
		memcpy(L->info,a,sizeof(Tsite));
		ordoneaza(&hash_map->vector[bucket_nr]);
		free(a);
		return a->ip;	
		}
	return NULL;
} 

int compKey(char *key, void *ae){		// compara key 
	Tsite a = *(Tsite*)ae;
	return strcmp(key, a.key);
}



int InsLDG(ALG aL, void* ae, size_t d){		//functia de inserare la sfirsit

	TLG L = *aL;
	TLG aux = malloc(sizeof(TCelulaG));
	if(!aux) return 0;
	aux->info = malloc(d);
	if (!aux->info){ 
		free(aux);
		return 1;
	}
	memcpy(aux->info, ae, d);
	if(L==NULL){        		 //lista vida 
		aux->pre=aux;
		aux->urm=aux;
		*aL=aux;
		return 1;	
	}

	else if(L==L->pre) {   		 //lista cu o celula	
		L->pre=aux;
		L->urm=aux;
		aux->urm=L;
		aux->pre=L;
		*aL=L;
		return 0;
	}
	else {
		aux->urm=*aL;		//lista cu celule >1
		aux->pre=L->pre; 
		L->pre->urm=aux; 
		L->pre=aux;      
		return 1;	
		}
}


void stergeultima(ALG aL){				//FUNCTIA DE STERGERE A ULTIMULUI ELEMENT
	if(*aL==NULL) return;   			//lista vida

	if(*aL==(*aL)->urm){				//lista cu o celula
		free((*aL)->info);
		*aL=NULL;
		return;
	}
	TLG L=*aL,aux;

	if(*aL==(*aL)->urm->urm){			//lista cu 2 celule
		aux=L->urm;
		L->urm=L;
		L->pre=L;
		*aL=L;
		free(aux->info);
		free(aux);
		return ;
	}

	L=L->pre;					//liste cu celule > 2 					
	aux=L;
	L->pre->urm = L->urm;			
	L->urm->pre = L->pre;
	free(aux->info);
	free(aux);
		
}

void removeL(THMG hash_map,char *key){ 			//funtia de stergere a elementului dupa "key"

	int hash_code = hash_function(key);
	int bucket_nr = hash_code % hash_map->M;
	TLG L=hash_map->vector[bucket_nr];
	ALG K=&hash_map->vector[bucket_nr];
	TLG aux;
	if (!L) return;                        		   //lista vida

	if (L==L->urm&&compKey(key,(L)->info)==0){  	   //lista cu o celula
			free(L->info);	
			free(L);
			L=NULL;
			hash_map->vector[bucket_nr]=L;
		return;
		}


		TLG ultimu=(L)->pre;
		for(; L!=ultimu; L = (L)->urm){		//liste cu celule >2
			if(compKey(key,(L)->info)==0){
				if(L==L->urm->urm){
					aux=L;
					L=L->urm;
					L->urm=L;
					L->pre=L;
					*K=L;
					free(aux->info);
					free(aux);		
				}
				else{
	    				L->pre->urm = L->urm;			
					L->urm->pre = L->pre;
					if(*K==L){
						*K=L->urm;
					}
					free(L->info);
					free(L);
				}		
				return;	
			}
		}
		
	
	if(compKey(key,L->info)==0){    	//lista cu 2 celule
		TLG aux = L;
		if(L==L->urm->urm){			
			L=L->pre;
			L->urm=L;
			L->pre=L;
			*K=L;
			free(aux->info);
			free(aux);
			return;
		}
	}

}


int put_hash(THMG hash_map,THMG hash_map2,int M){		//punem dintr-un tabel hash in altul
	int i;
	for (i = 0; i < hash_map2->M; i++) {
		ALG aL = &(hash_map2->vector[i]);
		if(*aL!=NULL) {
			
			if(*aL==(*aL)->urm){					//lista cu o celula
				Tsite p = *(Tsite*)(*aL)->info;
				set(p.key,p.ip,hash_map);
			}
			else{

				TLG ultimu=(*aL)->pre;				////lista cu celule>1
				for(; (*aL)!=ultimu; aL = &(*aL)->urm){	
					Tsite p = *(Tsite*)(*aL)->info;
					set(p.key,p.ip,hash_map);
				}
				Tsite p = *(Tsite*)(*aL)->info;
				set(p.key,p.ip,hash_map);
			}
		}

	}

	return 0;

}


void distrugetabelahash(AHMG hash_map){		//distruge tabelul hash

int i;
THMG aux = *hash_map;
	for (i = 0; i < aux->M; i++) {
		ALG curr_bucket = &(aux->vector[i]);
		Distruge(curr_bucket);
	}
	free(aux->vector);
	free(aux);
	aux = NULL;
}

void Distruge(ALG aL) {			//distruge lista
TLG aux,L=*aL;
	if (NULL==L) return;
	TLG inceput = *aL;
	TLG lista = *aL;
	if(lista==lista->urm){
		free(lista->info);		
		free(lista);
		*aL=NULL;
		return;
	}

	do {
		aux = lista->urm;
		free(lista->info);		
		free(lista);
		lista = aux;
	} while (lista != inceput);
		
	
}

void ordoneaza(ALG aL){					//functia de ordonare a listei
	if ((*aL)==NULL&&(*aL)==(*aL)->urm) return;	
	TLG lista1=(*aL),inceput=(*aL),lista2=(*aL),sf=(*aL)->pre;
	TLG aux=malloc(sizeof(TLG));
	aux->info=malloc(sizeof(Tsite));
	lista2=lista2->urm;
	Tsite p,q;
	do {
		p = *(Tsite*)lista1->info;
		do {
			q = *(Tsite*)lista2->info;
			if((p.Frequency< q.Frequency)||(p.Frequency == q.Frequency&&strcmp(p.key,q.key)>0)){ 
						//sortam dupa Frequency, daca sunt egale , sortam alfabetic
				  aux->info=lista1->info;
				  lista1->info=lista2->info;
				  lista2->info=aux->info;
				
				  p = *(Tsite*)lista1->info;
				  q = *(Tsite*)lista2->info;
			}
	
			lista2=lista2->urm;	
		}while (lista2 != inceput);	
	
	lista1=lista1->urm;
	lista2=lista1->urm;
	}while (lista1 != sf);

}

int num_tot(AHMG hash_map){			//functiea pentru numararea celulelor din tabelul hash
	int i,nr;
	THMG aux = *hash_map;
	for (i = 0; i < aux->M; i++) {	
		ALG curr_bucket = &(aux->vector[i]);
	  	nr+=numara_lista(curr_bucket);
	}
return nr;
}



int numara_lista(ALG aL){			//functiea pentru numararea celulelor din lista
	
	int n=0;
	if(!*aL){
		return 0;
	}
	TLG ultimu=(*aL)->pre;
	for(; (*aL)!=ultimu; aL = &(*aL)->urm){			
		n++;
	}
	n++;
	return n;
}







