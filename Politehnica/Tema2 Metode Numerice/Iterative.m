function R = Iterative(nume, d, eps)
fis=fopen(nume);      %dechid fisieru
	N=fscanf(fis,'%d',1); %cites numaru de noduri
	Mat=zeros(N,N);
	A=zeros(N,N);	%3 matrici N*N
	K=zeros(N,N);		  
	for i=1:N
		aux=fscanf(fis,'%d',1);		
		aux=fscanf(fis,'%d',1);	%citesc cite elemente sunt in nod
		Mat(i,1)=aux;				
		for j=1:aux
		Mat(i,j+1)=fscanf(fis,'%d',1);		%citesc elementele
	end
end

for i=1:N
	for j=2:N
		aux=Mat(i,j);		
		if aux>0
			A(i,aux)=1;				%Matricea de adiacenta
		end
end
end
R=zeros(N,1);
for i=1:N
	K(i,i)=Mat(i,1); 		%scriu gradele de iesire pe diagonla	
	if A(i,i)==1			%daca gasim pe deagonala 1 il stergim din K cu aceeasi pozitie si il facem 0
		K(i,i)--;				
		A(i,i)=0;
	end
	R(i)=1/N;				%vectorul R initial
end

M=(inv(K)*A)';				%M=calculat dupa formula


vect=ones(N,1);

while ( 1)
	auxR=R;					%R la pasul trecut
	R=(d*M)*auxR+(((1-d)/N))*vect;
	if (R-auxR)/norm(R)<eps	%conditia de oprire
		break;
	end

end

fclose(fis);				%inchid fisieru
end