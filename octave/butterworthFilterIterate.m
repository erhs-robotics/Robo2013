
pkg load all; % Load the required signal processing toolbox

% sampling frequency in Hz
samp_freq = 1500;

% nyquist frequency, half the sampling frequency
nyq_freq = samp_freq/2;

for j=1:10;
	%cutoff_freq=j %use this line to test every integer in the range (line 10)
	testabove = 1 %change this line set a number to test every tenth 
	%(i.e. 3 would result in the values being 3.1, 3.2 etc.) 
	cutoff_freq=(j/10)+ testabove % use this line to enable testabove parameter
	
	

	% create a first-order Butterworth low pass.
	% The returned vectors are of legth n.
	% Thus a first order filter is created with n = 2.
	[b,a]=butter(2, cutoff_freq/nyq_freq);

	% create a 5 seconds signal with 3 parts:
	% a 1 Hz and a 200 Hz sine wave and some gaussian noise.
	t=0:1/samp_freq:5;
	wave=sin(2*pi*t) + sin(2*pi*200*t); 
	noise = randn(size(t));
	input = wave + noise;
	% apply the filter
	output=filter(b,a,input);
	figure;
	hold;
	plot(input,"2")
	plot(wave,"1")
	plot(output,"3")
	
	
end
print(1,"butterworth_filter1");
print(2,"butterworth_filter2");
print(3,"butterworth_filter3");
print(4,"butterworth_filter4");
print(5,"butterworth_filter5");
print(6,"butterworth_filter6");
print(7,"butterworth_filter7");
print(8,"butterworth_filter8");
print(9,"butterworth_filter9");
print(10,"butterworth_filter10");
