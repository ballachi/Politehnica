/*Bulachi Marin - 311CB*/
/*-- Hash.c -- hash table */
#include "LDG.h"



int main(int num, char *argumentu[]){ 


FILE *hash_in = fopen(argumentu[2],"r");			//deschidem fisieru input
FILE *hash_out= fopen(argumentu[3],"w");			//deschidem fisieru ouput
int M = atoi(argumentu[1]),index,numarul,m;			//dimensiunea M
char *key=malloc(200),*ip=malloc(200),set_get[200],*f=malloc(200);
THMG hash_map = NULL;
hash_map = alocaHashMap(M);					//aloc tebela hash

while (fscanf(hash_in,"%s",set_get) != EOF){
	
	if (strcmp(set_get,"set")==0){						//SET
		fscanf(hash_in,"%s %s",key,ip);					
		numarul=num_tot(&hash_map);					//numarul de elemente
		if(numarul>M*2){						//daca numar de elemente e mai mare ca M*2
			
			THMG hash_map2 = NULL;
			hash_map2 = alocaHashMap(M);				//alocam o tabel hash2 noua
			put_hash(hash_map2,hash_map,M);				//punem in tabela noua listele din tabela veche
			m=M;							
			M=M*2;							
			distrugetabelahash(&hash_map);				//distrugem tabela hash		
			hash_map = alocaHashMap(M);				//alocam tabela hash 
			put_hash(hash_map,hash_map2,m);				//punem in tabela hash informatia celule din tabela hash2
			distrugetabelahash(&hash_map2);				//distrugem tabela hash2
 
		}

		set(key, ip, hash_map);						//punem in tabela hash informatia citita din fisier
	}

	if (strcmp(set_get,"get")==0){						//GET
		fscanf(hash_in,"%s",key);	
		f=get(key, hash_map);						//punem in f ip-ul corespunzator key 
		if (f==NULL){
			fprintf(hash_out,"NULL\n");				//daca e NULL scriem in fisier "NULL"
		}
		else{
			fprintf(hash_out,"%s\n",f);				//scriem in fisier ip primit 
		}	
	}

	if (strcmp(set_get,"print")==0){					//PRINT
		printHashMap(hash_map,hash_out);
	}

	if (strcmp(set_get,"print_list")==0){					//PRINT_LIST
		fscanf(hash_in,"%s",key);
		index =atoi(key);
		if(index>=0&&index<M){
			Afisareindex(&hash_map->vector[index],hash_out,index);
		}
	}	

	if (strcmp(set_get,"remove")==0){					//REMOVE
		fscanf(hash_in,"%s",key);
		removeL(hash_map,key);
	}
	
}

free(key);
free(ip);
distrugetabelahash(&hash_map); 
fclose(hash_in);
fclose(hash_out);
return 0;
}
