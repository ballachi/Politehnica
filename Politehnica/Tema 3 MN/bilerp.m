function value = bilerp(img, row, col)

	[n m]=size(img)  ;
	if (1 <= row && row<=n) && (1 <= col && col<=m)      %verificam daca indicele exista
		rind = floor(row);			     
		coloana = floor(col);
		if (coloana != m)
			y=col;				%daca indicele col diferit de ultimu element
			y1=coloana;			%y va fi col ,y1 intregu,y2 intregu+1;
			y2=y1+1;
		else 
			y=col;				%daca indicele col = ultimu element
			y1=coloana - 1;			%y va fi col ,y1 intregu -1 pentru a nu fi egal cu col,y2 =col
			y2=y1+1;
		end
		if (rind != n)				%la fel pentru row
			x=row;
			x1=rind;
			x2=x1+1;
		else
			x=row;
			x1=rind-1;
			x2=x1+1;
		
		end
	
		a=(x2 - x1)*(y2 - y1);
		%FORMULA PENTRU CALCUL
		F=img(x1,y1)*((x2 - x)*(y2 - y))/a+img(x2,y1)*((x - x1)*(y2 - y))/a+img(x1,y2)*((x2 - x)*(y - y1))/a+img(x2,y2)*((x - x1)*(y - y1))/a;
		value = F	;
	else 
		value = 0;	%CAZUL IN CARE INDECELE NU EXISTA
	end

end
