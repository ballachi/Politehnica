function [R1 R2 R3 ] =PageRank(nume,d,eps)
	fis=fopen(nume);							%deschid fisieru
	N=fscanf(fis,'%d',1);						%citesc numarul de noduri
	R1 = Iterative(nume,d,eps);					%apelez functia Iterative
	output=strcat(nume,'.out');					%concatenez .out la numele fisierului 
	outfis=fopen(output,'w');					%deschid fisierul pentru scriere
	fprintf(outfis,'%d\n',N);					%pe prima linie N
	fprintf(outfis,'\n');	
	fprintf(outfis,'%f\n',R1);					%EX1

	R2 = Algebraic(nume,d);
	fprintf(outfis,'\n');
	fprintf(outfis,'%f\n',R2);					%EX2

	R3 = Power(nume,d,eps);
	fprintf(outfis,'\n');
	fprintf(outfis,'%f\n',R3);					%EX3


	Mat=zeros(N,N);
	for i=1:N
		aux=fscanf(fis,'%d',1);
		aux=fscanf(fis,'%d',1);					%citesc cite elemente sunt in nod
		Mat(i,1)=aux;
		for j=1:aux
			Mat(i,j+1)=fscanf(fis,'%d',1);		%citesc elementele	
		end
	end
	x=R2;										%copiez vectorul R2 in X
pozi=zeros(1,N);
pozi=1:N;

n = length(x);
 for i = 1 : n 									%sortez vrecorul X
	for j = 1 : n
     
        if (single(x(i)) > single(x(j)))
            val = x(i);
			x(i) = x(j);
			x(j) = val;
			val2=pozi(i);
			pozi(i) = pozi(j);					%cind voi schimba elementele voi schimba si pozitiile
			pozi(j)=val2;
          
        end
    end
end

	val1=fscanf(fis,'%f',1);
	val2=fscanf(fis,'%f',1);
	R4 (1,:)=1:N;  								%prima linie va fi  poziti de la 1 :N
	R4 (2,:)=pozi;								%a doua linie va fi pozitia elemetelor inaite de sortare
	R4(3,:)=Apartenenta(x,val1,val2);			%petru linia 3 apelam functia 
	fprintf(outfis,'\n');
	fprintf(outfis,'%d %d %f\n',R4);			%scrim in fisier

	fclose(outfis);								%inchid fisierele
	fclose(fis);

endfunction