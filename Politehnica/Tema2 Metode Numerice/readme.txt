Ex 1 : Iterative
	citesc prima linie si aflu cite noduri sunt
	citesc N linii si le scriu intr-o matrice
	din matrice le scriu in A matricea de adiacenta
	scriu cite elemente are fiecare nod in matricea K
	calculez M dupa formula
	calculez iterativ R
	conditia de esire va fi R(t+1)-R(t)/norm(R(t+1))

Ex 2 : Algebraic
	citesc prima line si aflu cite noduri sunt
	citesc N linii si le scriu intr-o matrice
	din matrice le scriu in A matricea de adiacenta
	scriu cite elemente are fiecare nod in matricea K
	calculez M dupa formula
	calculez inversa lui (In -d * M) cu Gram Schmidt modificat,functia  Gram_Schmidt imi intoarce inversa matricei (In -d * M) 
	calculez R dupa formula

Ex 3 : Power
	citesc prima linie si aflu cite noduri sunt
	citesc N linii si le scriu intr-o matrice
	din matrice le scriu in A matricea de adiacenta
	scriu cite elemente are fiecare nod in matricea K
	calculez M dupa formula
	calculez iterativ R
	coditia de esire va fi R(t+1)-R(t)/norm(R(t+1))

Ex 4 :Apartenenta
	Am calculat sistemu a*val1+b=0
						a*val2+b=1 

	parcurg cu un for fiecare valoare a vectorului X
	daca se afla in intervalul [0,val1) atunci y(i)=0;
	daca se afla in intervalul [val1,val2]  atunci y(i)=(a)*x(i,1)+b;
	daca se afla in intervalul (val2,1] atunci y(i)=1;

PageRank 
	scrie R1 R2 R3 in fisier
	ordoneaza R2 
	il transmite ordonat ca parametru petru a afla R4
	scrie R4 in fisier

