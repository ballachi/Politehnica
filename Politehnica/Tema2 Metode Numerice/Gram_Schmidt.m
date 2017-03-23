function [A] = Gram_Schmidt(A) %algoritm 
	[m n] = size(A);   
	Q = zeros(m,n);
	R = zeros(n);
	for i = 1 : n
		R(i,i) = norm(A(:,i));			
		Q(:,i)=A(:,i)/R(i,i);
		for j = i +1 : n
			R(i,j)=Q(:,i)'*A(:,j);
			A(:,j)=A(:,j)-Q(:,i)*R(i,j);
		endfor
	endfor

	[n, n] = size(A);
	x = zeros(n,1);
	I = eye(n);
	for j = 1 : n
		b = I(:,j);
		x(n) = b(n)/R(n,n);
		for i = n-1:-1:1
			x(i)=(b(i)-R(i,i+1:n)*x(i+1:n))/R(i,i);  
		end
		RR(:,j) = x;  %aflam inversa lui R
	end
	A=RR * Q';  %inversa lui A =inversa lui R * Q transpus
endfunction
