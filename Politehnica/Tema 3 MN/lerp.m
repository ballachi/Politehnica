function value = lerp(v, x)

    % Interpolare liniara unidimensionala
    %
    % Inputs
    % ------
    % v: un vector cu valorile functie
    % x: o noua pozitie in care sa se calculeze valoarea functiei
	l=length(v);   %LUNGIMEA VECTORULUI
 	if 1 <= x && x<=l     
		n = floor(x);	%PARTEA INTREAGA
		eta = x-n;	%PARTA ZECIMALA
		v = [v 0];
		value = (1-eta).*v(n) + eta.*v(n+1); %calulul in partea intrega apoi in partea intrega +1
	else
		value=0;
	end
   
end
