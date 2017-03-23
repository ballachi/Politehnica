function demo()
	
	x=10;
	T=[cos(x*pi/180) -sin(x*pi/180);sin(x*pi/180) cos(x*pi/180)];
	img_out=forward_mapping('flapping_duck.png' ,T);
	imwrite(mat2gray(img_out), '10_grade_duck.png');
	x=45;
	T=[cos(x*pi/180) -sin(x*pi/180);sin(x*pi/180) cos(x*pi/180)];
	img_out=forward_mapping('flapping_duck.png' ,T);
	imwrite(mat2gray(img_out), '45_grade_duck.png');
	x=90;
	T=[cos(x*pi/180) -sin(x*pi/180);sin(x*pi/180) cos(x*pi/180)];
	img_out=forward_mapping('flapping_duck.png' ,T);
	imwrite(mat2gray(img_out), '90_grade_duck.png');
	x=180;
	T=[cos(x*pi/180) -sin(x*pi/180);sin(x*pi/180) cos(x*pi/180)];
	img_out=forward_mapping('flapping_duck.png' ,T);
	imwrite(mat2gray(img_out), '180_grade_duck.png');



end
