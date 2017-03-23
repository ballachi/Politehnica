#include <string.h>
#include <stdlib.h>
#include <time.h>
#include <stdio.h>


typedef struct {
	char SI[15];
	char caracter[5];
	char P;
	char SU[15];

} Linie;


int main(){
	FILE *tm=fopen("tm.in","r");
	FILE *tape=fopen("tape.in","r");
	FILE *tapeout=fopen("tape.out","w");

	int N,M,P,i,j,lungime,q;
	fscanf(tm,"%d",&N);
	char stari[N][7],stareInitiala[7],banda[10000];
	for(i=0;i<N;i++){
		fscanf(tm,"%s",stari[i]);
	}
	//gata cu prima linie

	fscanf(tm,"%d",&M);
	for(i=0;i<M;i++){
		fscanf(tm,"%s",stari[i]);
		stari[i][strlen(stari[i])]='\0';
	}
	//gata cu a doua linie

	fscanf(tm,"%s",stareInitiala);
	stareInitiala[strlen(stareInitiala)]='\0';

	fscanf(tape,"%s",banda);
	lungime=strlen(banda);

	fscanf(tm,"%d",&P);
	Linie linia[P];

	for(i=0;i<P;i++){

		fscanf(tm,"%s",linia[i].SI);

		fscanf(tm,"%s",&linia[i].caracter[0]);

		fscanf(tm,"%s",linia[i].SU);

		fscanf(tm,"%s",&linia[i].caracter[1]);
		
		fscanf(tm,"%c",&linia[i].P);

		fscanf(tm,"%c",&linia[i].P);
		linia[i].SI[strlen(linia[i].SI)]='\0';
		linia[i].SU[strlen(linia[i].SU)]='\0';
		linia[i].caracter[strlen(linia[i].caracter)]='\0';
	}

	i=0;
	while(banda[i]=='#'){
		i++;
	}
	 int ii=strlen(banda);;
	 while(ii<10000){
	 	banda[ii]='#';
	 	ii++;
	 }

	int numB;
	int conditie=0;
	while(conditie==0){
		for(j=0;j<=P;j++){
			numB=strlen(banda);
			if (i==numB){
				banda[i]='#';

			}
			if(strcmp(stareInitiala,linia[j].SI)==0&&banda[i]==linia[j].caracter[0]){

				strcpy(stareInitiala,linia[j].SU);
				banda[i]=linia[j].caracter[1];
				if(linia[j].P=='H'){
					i=i;
				}
				else if(linia[j].P=='R'){
				i++;
				}
				else if(linia[j].P=='L')
					i--;
				for(q=0;q<M;q++){
					if(strcmp(stareInitiala,stari[q])==0){
						conditie++;
					}
				}
				break;
			}
			if (j==P){
					fprintf(tapeout,"Se agata!");
					return 0;
			}
		}
	}
	
	numB=strlen(banda);
	if (banda[numB-1]!='#'){
		banda[numB]='#';
	}
	fprintf(tapeout,"%s",banda );


	fclose(tm);
	fclose(tapeout);
	fclose(tape);
	return 0;
	
}