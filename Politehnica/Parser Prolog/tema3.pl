valnum(X):- char_type(X, alnum), char_type(X, ascii).
vother(X):- member(X, [';','<','+','-','*','(',')','{','}']).
validc(X):- valnum(X) ; vother(X) ;  X == '='.

lparseq(['='|L],'==',L).
lparseq([X|L],'=',[X|L]):-dif(X,'=').
lparseq([],'=',[]).

lparsealn([X|L],L2,R,L3):- valnum(X), lparsealn(L, [X|L2], R, L3).
lparsealn([X|L],L2,R,[X|L]):- \+valnum(X), reverse(L2, L3), atom_chars(R, L3).
lparsealn([],L2,R,[]):- reverse(L2, L3), atom_chars(R, L3).

lparse2(['='|L],L2,L3):- lparseq(L,R,L4), lparse2(L4,[R|L2],L3).
lparse2([X|L],L2,L3):- valnum(X),lparsealn(L,[X],R,L4), lparse2(L4,[R|L2],L3).
lparse2([X|L],L2,L3):- vother(X), lparse2(L,[X|L2],L3).
lparse2([X|L],L2,L3):- \+validc(X), lparse2(L,L2,L3).
lparse2([],L2,L3):- reverse(L2,L3).

lparse(S, L):- atom_chars(S, L2), lparse2(L2,[],L),!.



parseInput(F,R):-read_file_to_string(F,S,[]), lparse(S,L), myatom_number(L,L1),
 				parseInputAux(L1, [], R ,_) , (((number(R)); (R=='e'); R=='a') ; unif('x',R)) , !.


%%------------------------------------------------------------------------------------------------------------------
%%------------------------------------------------------------------------------------------------------------------
%%------------------------------------------------------------------------------------------------------------------


parseInputAux([],VAR,_,VAR).

%% Extrag expresia si o evaluez 
parseInputAux([return|L], VAR, R ,VAR):-   get_expr(L,E1 ,_), eval_expr(E1,VAR,R).

%% Extrag expresia si o evaluez , adaug variabila X in lista de variabile,evaluez programul ramas
parseInputAux([X|['='|L]], VAR, R,VARR2) :- get_expr(L,E1,LF) , 
										eval_expr(E1,VAR,R1) ,
 										addvar(X, R1, VAR, VAR2) ,
 										parseInputAux(LF, VAR2 , R, VARR2) ,!.

%% Extrag expresia din stinga si din dreapta le evaluez si extrag operatia, evaluez expresile
%% verific expresile. (R4==1 ) == true. evaluez programul ramas.
parseInputAux([assert|L], VAR, R ,VARRET):- get_expr(L, E1, LF) ,get_opr(L, OP) ,
  											get_expr(LF, E2, LF2) ,eval_expr(E1, VAR, R1) , 
  											eval_expr(E2, VAR, R2) , verifica(R1, OP, R2, R4) ,
  											R4==1, parseInputAux(LF2, VAR, R, VARRET),! .


%% Eroare la evaluarea assert-ului.
parseInputAux([assert|L], VAR, 'e' ,VAR):- get_expr(L, E1, LF), get_opr(L, _),
 											get_expr(LF, E2,_), eval_expr(E1, VAR, R1),
 											eval_expr(E2, VAR, R2), (R1 == 'e' ; R2 == 'e') .
  
%% In caz ca asertu este fals ,se evalueaza programul in continuare si se verifica daca nu a fost gasita o eroare
parseInputAux([assert|L], VAR, R ,VARRET):- get_expr(L,E1,LF), get_opr(L,OP) ,
 										 get_expr(LF,E2,LF2), eval_expr(E1,VAR,R1) ,
 										 eval_expr(E2,VAR,R2), verifica(R1,OP,R2,R4) ,
 										 R4==0,parseInputAux(LF2, VAR, R5 ,VARRET)  
 										 ,((R5 == 'e' ,unif(R5,R)) ;( unif('a',R))), !.

%% Extrag expresia din stinga si din dreapta le evaluez si extrag operatia, evaluez expresile
%% Fac un sau .
%% In ambele parti se evalueaza programul ramas dupa,si se verifica de erori.
parseInputAux([if|L], VAR, R , VARRET2):- get_expr(L,E1,LF) ,get_opr(L,OP),
 								 get_expr(LF,E2,LF2) ,eval_expr(E1,VAR,R1) , 
 								 eval_expr(E2,VAR,R2)  ,verifica(R1,OP,R2,R4) ,(
 								 	R4==1,  get_if_prog(LF2,E3,LF3) ,get_if_prog(LF3,_,LF4)
 								 		, parseInputAux(E3,VAR,RAUX2,VARRET), ( 
 											(RAUX2 == 'e' , unif(RAUX2,R) , !) ; (number(RAUX2), unif(RAUX2,R),!) ; 
 											(RAUX2 == 'a' , parseInputAux(LF4,VARRET,R5,VARRET2) ,( (R5 == 'e',unif(R,R5)); (unif(RAUX2,R)) ) ) ;
 											  (parseInputAux(LF4,VARRET,R,VARRET2),!) ) ;
 								 R4==0,  get_if_prog(LF2,_,LF3) ,get_if_prog(LF3,['else'|E4],LF4) ,
 								 		parseInputAux(E4,VAR,RAUX,VARRET) , ( 
 											(RAUX == 'e' , unif(RAUX,R) , !) ; (number(RAUX), unif(RAUX,R),!) ; 
 											(RAUX == 'a' , parseInputAux(LF4,VARRET,R5,VARRET2) ,( (R5 == 'e',unif(R,R5)); (unif(RAUX,R)) ) ) ;
 											parseInputAux(LF4,VARRET,R,VARRET2))) ,!. 
%% Evaluez asignarea.
%% Extrag conditia, asignarea, programul.
%% Evaluez for-ul
%% Evaluez programul ramas.
parseInputAux([for|L], VAR, R , VARRET2) :-  	get_expr(L,E1,LF) ,asig_for(E1,VAR,VAR2) , get_code(LF,COND,LF2) ,
 												get_code(LF2,INC,['{'|LF3]),get_prog(LF3,0,PRO,LF4),
 												eval_for(COND, INC, PRO,_, VAR2 , VAR3) , parseInputAux(LF4,VAR3,R,VARRET2), ! . 


parseInputAux(_,VAR,_,VAR). 

%% evaluez for-ul cit timp conditia este adevarata.
eval_for(COND, INC, PRO, R, VAR , VAR2):- get_expr(COND,E1,LF) ,get_opr(COND,OP) ,
 										 get_expr(LF,E2,_) ,eval_expr(E1,VAR,R1) 
 										 , eval_expr(E2,VAR,R2) , verifica(R1,OP,R2,R4) ,
 										  R4==1 , parseInputAux(PRO,VAR,_,VAR3) , parseInputAux(INC,VAR3,R,VAR4) ,
 										  eval_for(COND, INC, PRO, _, VAR4 , VAR2) , !.

%% conditia falsa
eval_for(COND, _, _, _, VAR , VAR):- get_expr(COND,E1,LF) ,get_opr(COND,OP) ,
 										 get_expr(LF,E2,_) ,eval_expr(E1,VAR,R1) 
 										 , eval_expr(E2,VAR,R2) , verifica(R1,OP,R2,R4),R4==0,!.

asig_for([X|['='|L]], VAR, VAR2) :-  get_expr(L,E1,_) , 
										eval_expr(E1,VAR,R1) ,
 										addvar(X, R1, VAR, VAR2).



%% N numarul de paranteze
%% Extrag programul din for.
%% in R1 se va unifica cu programul ramas dupa for.
get_prog(['}'|L],0,[],L) .
get_prog(['{'|L],N,['{'|R],R1) :- NEWN is N+1 ,get_prog(L,NEWN,R,R1),!.
get_prog(['}'|L],N,['}'|R],R1) :- NEWN is N-1, get_prog(L,NEWN,R,R1),!.
get_prog([X|L],N,[X|R],R1) :- get_prog(L,N,R,R1),!.


%% Extrag operatia
get_opr(['=='|_],'==').
get_opr(['<'|_],'<') . 
get_opr([_|L],L2) :- get_opr(L,L2),!.

%% verific conditia
verifica(X,'==',Y,0):- X \= Y.
verifica(X,'==',Y,1):- X == Y.
verifica(X,'<',Y,0):- X >= Y.
verifica(X,'<',Y,1):- X < Y.

%% va inlocui intro expresie toate variabilele cu valoarea lor.
get_expr_non_var([],_,[]) .
get_expr_non_var([X|L],VAR,[X|R]) :- number(X), get_expr_non_var(L,VAR,R),!.
get_expr_non_var([X|L],VAR,[X|R]) :- vother(X), get_expr_non_var(L,VAR,R),!.
get_expr_non_var([X|L],VAR,[Y|R]) :- getvar(X,VAR,Y) , get_expr_non_var(L,VAR,R),!.

%% unificarea
unif('e' , 'e').
unif('a' , 'a').
unif(X,X).

%% adaug o variabila in lista.
addvar(X,Y,[],[[X,Y]]).
addvar(X, Y, [ [X, Z] | L], R) :- Z \= X , addvar(X,Y,L,R),!.
addvar(X, Y, [ [Z, W] | L], [ [Z, W] |R]) :- addvar(X,Y,L,R),!.

%% Extrag valoarea
getvar(_,[],'e').
getvar(X,[[X,N]|_],N).
getvar(X,[[Y,_]|L],R) :- Y \= X , getvar(X,L,R),!.


%% Intoarce expresia pina la ';' || ')'
get_code([')'|L] , [';'], L).
get_code([';'|L] , [';'], L).
get_code([X|L],[X|R],R1) :- get_code(L,R,R1),!.


%% Intoarce programul din if
get_if_prog(['}'|L],[],L).
get_if_prog(['then'|L],R,R1) :- get_if_prog(L,R,R1),!.
get_if_prog(['{'|L],R,R1) :- get_if_prog(L,R,R1),!.
get_if_prog([X|L],[X|R],R1) :- get_if_prog(L,R,R1),!.


%% Extrage expresia
get_expr(['}'|L],[';'],L).
get_expr([';'|L],[';'],L).
get_expr([')'|[ ';' |L]],[';'],L).
get_expr([')'|L],[';'],L).
get_expr(['=='|L],[';'],L).
get_expr(['<'|L],[';'],L).
get_expr(['then'|L],R,R1) :- get_expr(L,R,R1),!.
get_expr(['('|L],R,R1) :- get_expr(L,R,R1),!.
get_expr(['{'|L],R,R1) :- get_expr(L,R,R1),!.
get_expr([X|L],[X|R],R1) :- get_expr(L,R,R1),!.
get_expr(_,_,_) .


%% evalueza expresia 
%% Se inloceste toate variabilele cu valoarea lor
%% Se face inmultirea, apoi scadearea , si apoi din lista ramasa se face adunarea si se returneaza un raspuns.
eval_expr(L, VAR, R) :- get_expr_non_var(L,VAR,E) ,valide(E,R2), R2 == true  , eval_mul(E,E1), eval_min(E1,E2), eval_add(E2,R) ,!.
eval_expr(L, VAR, 'e') :- get_expr_non_var(L,VAR,E) ,valide(E,R), R == false  , !.

%% Se verifica daca in lista au fost toate varibilele declarate.
valide([],true).
valide([E|L],R) :- E \= 'e' , valide(L,R),!. 
valide([E|_],false) :- E == 'e' .



%% Se face adunarea si  R se unifica cu un numar ,ramas in lista.
eval_add([E|[';'|_]],E) .
eval_add([E|['+'|[E2|L]]],R):- add_elem(E,E2,R1) ,eval_add([R1|L],R),!.
eval_add(_,_).


%% Se face doar operatia de scadere din lista, se intoarce lista fara operatia de scadere
eval_min([],[]) .
eval_min([E|['+'|[E3|L]]],[E|['+'|R]]) :- eval_min([E3|L] , R),!.
eval_min( [E|['-'|[E3|['+'|L]]]] , [M|['+' |R]]) :-  min_elem(E,E3,M), eval_min(L , R),!.

eval_min( [E|['-'|[E3|[';'|_]]]] , [M|[';'|[]]]) :-  min_elem(E,E3,M) ,!.
eval_min( [E|['-'|[E3|['-'|L]]]] , R) :-  min_elem(E,E3,M), eval_min([M|['-'|L]] , R),!.
eval_min([E|[';'|_]],[E|[';'|[]]]) .


%% Se face doar operatia de inmultire din lista, se intoarce lista fara operatia de inmultire.
eval_mul([],[]).
eval_mul([E|['+'|[E3|L]]], [E|['+'|R]]) :- eval_mul([E3|L] , R),!.
eval_mul([E|['-'|[E3|L]]], [E|['-'|R]]) :- eval_mul([E3|L] , R),!.
eval_mul( [E|['*'|[E3|['+'|L]]]] , [M|[ '+'|R]]) :-  mul_elem(E,E3,M), eval_mul(L , R),!.

eval_mul( [E|['*'|[E3|['-'|L]]]] , [M|[ '-'|R]]) :-  mul_elem(E,E3,M), eval_mul(L , R),!.
eval_mul( [E|['*'|[E3|[';'|_]]]] , [M|[';'|[]]]) :-  mul_elem(E,E3,M) .
eval_mul( [E|['*'|[E3|['*'|L]]]] , R) :-  mul_elem(E,E3,M), eval_mul([M|['*'|L]] , R),!.
eval_mul([E|[';'|_]],[E|[';'|[]]]) .



add_elem(X,Y,R) :- R is X + Y .
mul_elem(X,Y,R) :- R is X * Y .
min_elem(X,Y,R) :- R is X - Y .

%% Toate numerele care sun atom le transform in numar
myatom_number([],[]).
myatom_number([E|L],[E1|R]) :- atom_number(E, E1) , myatom_number(L,R),!.
myatom_number([E|L],[E|R]) :- myatom_number(L,R),!.

