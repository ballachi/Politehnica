function [rezultat] = rgbHistogram(Imag,c_b)

photo=double(imread(Imag));	
r=photo(:,:,1);		%fiecare culoare intro matriceculoarea 
g=photo(:,:,2);		
b=photo(:,:,3);	
[ n m ] = size(g);
 				
vect_r=zeros(1,c_b );   %vectorii care ii voi returna
vect_b=zeros(1,c_b ); 		
vect_g=zeros(1,c_b );		
	
for i= 1 : n		
	for j = 1 : m					
		k=r(i,j);			%punem valoarea intr-o variabila 
		a=floor( k * c_b / 256);	%calculam indicele , (floor ne intorce doar intregii) 
		vect_r(++a)++;                  %marim cu +1  valoarea vectorului cu indexul a,(++a) deoarece sa evitam a=0;

		k=g(i,j);		
		a=floor( k * c_b / 256);				
		vect_g(++a)++;

		k=b(i,j);		
		a=floor( k * c_b / 256);				
		vect_b(++a)++;			
	endfor
endfor

rezultat=[vect_r vect_g vect_b];		%cele trei liste in ordinea r,g,b

endfunction
