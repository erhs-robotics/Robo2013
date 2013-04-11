#leave this alone
wave = [-pi:0.01:pi];
length = 629;
noise = 2*randn(1,length);
signal = wave+noise;
#end
coef = 1; #put this in your function for the adjustable value
output = [1:length]; #leave this alone
for j = 1:length;
	output(j) = signal(j)+1#put your function here with signal(j) as the input
end;

plot(output);
print("your_results")
	
