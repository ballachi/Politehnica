function img_out = forward_mapping(img_in, T)
  
im=imread(img_in); %scriu imaginea in matrice

[m,n]=size(im);
img_out=uint8(zeros(m,n)); 
xc=ceil(n/2);		%xc va fi jumatate din linii
yc=ceil(m/2);		%yc jumatate din coloane

eror2=((1-xc)*T(2,1)+(1-yc)*T(2,2)+yc);   %calculez de unde treb sa incep sa scriu in imagine 
eror3=((n-xc)*T(2,1)+(n-yc)*T(2,2)+yc);

e2=(1-xc)*T(1,1)+(1-xc)*T(1,2) +xc ;
e3=(n-yc)*T(1,1)+(n-yc)*T(1,2) +xc ;
eror=min(min(min(e2,e3),eror2),eror3);	%cel ma mic numar 
if eror<0
eror=round(eror*(-1)+2)	;		%daca e negativ il inmultim cu -1  
else 
eror=2;					%daca e pozitiv atunci eroarea maxima va fi 1
end
for i=1:m				%parcurgem imaginea
   for j=1:n
      i1=i-xc; 				
      j1=j-yc; 
      x1=i1*T(1,1)+j1*(T(1,2)) +xc ;	%calculam pozitile 
      y1=i1*T(2,1)+j1*T(2,2)+yc;
      x2=round(x1) 	;		%rotungim pozitile
      y2=round(y1)  ;
      if x2+eror>=1			
      img_out(x2+eror,y2+eror)=im(i,j);	%scriem in matricea de esire
      end
   end 
end
end
