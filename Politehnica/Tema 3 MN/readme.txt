Am facut 4 functii lerp.m ,bilerp.m ,forward_mapping.m,demo.m

lerp
	daca pozitia x se afla in vector v
	Calculez  partea intrega si partea zecimala
	inmultesc (1-partea zecimala) cu valoarea v(cu indicele intreg)+(partea zecimala)* cu valoarea v(cu indicele intreg+1)

bilerp
	verificam daca indicele exista
	daca indicele col diferit de ultimu element
		y va fi col ,y1 intregu,y2 intregu+1;
	daca indicele col = ultimu element
		%y va fi col ,y1 intregu -1 pentru a nu fi egal cu col,y2 =col
	la fel  pentru row
	calculez valoarea

forward_mapping
	calculez prima si ultima pozitie x  si prima si ultima pozitie y , calculez minimu ,incep a scrie in matrice din cel mai mic numar negativ
	parcurg imaginea calculez pozitiile si scriu in matrice

demo 
	se apeleaza fara parametri
	scrie 4 imagini cu 10,45,90,180 grade rotite 
	rotirea se face cu ajutorul radianelor 	
	x=45;
	T=[cos(x*pi/180) -sin(x*pi/180);sin(x*pi/180) cos(x*pi/180)];
