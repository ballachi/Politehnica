function [rezultatul] = hsvHistogram(Imag,c_b)

photo=imread(Imag);
r=photo(:,:,1);		%fiecare culoare intro matriceculoarea
g=photo(:,:,2);
b=photo(:,:,3);
[ n m ]=size(g);

vect_h=zeros(1,c_b);	%vectorii care ii voi returna
vect_s=zeros(1,c_b);
vect_v=zeros(1,c_b);

for i = 1 : n	
	for j = 1 : m							%______Algoritmul
 		r_prim=double(r(i,j))/255;
	 	g_prim=double(g(i,j))/255;
	        b_prim=double(b(i,j))/255;
		cmax=max(r_prim,max(g_prim,b_prim));
		cmin=min(r_prim,min(g_prim,b_prim));
		delta=cmax-cmin;
	
		if delta == 0
			H=0;
		else
			if cmax == r_prim
				H=60*mod(((g_prim-b_prim)/delta),6);
			endif
			if cmax == g_prim
				H=60*(((b_prim-r_prim)/delta)+2);
			endif
			if cmax == b_prim
				H=60*(((r_prim-g_prim)/delta)+4);
			endif
		endif

		if cmax == 0
			S=0;
		else
			S=delta/cmax;
		endif

		V=cmax;
		indice_h=floor(double(H)/(double(364)/c_b));   %calculam indicele care va fi un numar real 
		indice_s=floor(double(S*100)/(101/c_b));
		indice_v=floor(double(V*100)/(101/c_b));
		
		vect_h(++indice_h)++;	%marim cu +1  valoarea din vector cu indexul indice_h ,(++indice_h) deoarece sa evitam (indice_h)=0;
		vect_s(++indice_s)++;
		vect_v(++indice_v)++;
		
	endfor	
endfor
	rezultatul=[vect_h vect_s vect_v];		%cele trei liste in ordinea h,s,v
	endfunction


















					





