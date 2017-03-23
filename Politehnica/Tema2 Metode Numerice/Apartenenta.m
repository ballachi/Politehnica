function y = Apartenenta(x, val1, val2)
	% Am calculat sistemu a*val1+b=0
	%					  a*val2+b=1		
	b=-(val1/(val2-val1));	
	a=-b/val1;

	n=length(x);	%lungimea vectorului
	
	for i=1:n
	if 0.000000 <= x(i,1) && x(i,1) <val1    %daca se afla in intervalul [0,val1)
		(y(i))=0;
	end

	if val1 <= x(i,1) && x(i,1) <= val2  	%daca se afla in intervalul [val1,val2]
		y(i)=(a)*x(i,1)+b;
	end

	if val2 < x(i,1) && x(i,1) <= 1.000000	%daca se afla in intervalul (val2,1]
		(y(i))=1;
	end

	end
	y=y';
end