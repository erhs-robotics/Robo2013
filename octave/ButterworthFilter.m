pkg load all; % Load the required signal processing toolbox

setpoint = 20;
variance = 1;
oscillation_freq = 20;
oscillation_magnitude = 1;

% sampling frequency in Hz
samp_freq = 800;
oversamp_freq = samp_freq * (2**4);

% nyquist frequency, half the sampling frequency
nyq_freq = samp_freq/2;
nyq_freq2 = oversamp_freq/2;

% cut off frequency, in Hz, between 0 and nyquist frequency
% try playing around and changing this value guys
cutoff_freq=5;

% create a first-order Butterworth low pass.
% The returned vectors are of legth n.
% Thus a first order filter is created with n = 2.
[b,a]=butter(2, cutoff_freq/nyq_freq);

% create a 5 seconds signal with 3 parts:
% a 1 Hz and a 20 Hz sine wave and some gaussian noise.
t=0:1/samp_freq:5;
input=setpoint + sin(2*pi*oscillation_freq*t)*oscillation_magnitude + randn(size(t)) * variance;
plot(t, input);
print("nofilter");

output=filter(b,a,input);
plot(t, [output]);
print("normsamp");

% adjust butter sample to oversample frequencies
[b,a]=butter(2, cutoff_freq/nyq_freq2);

% oversample time interval
t2=0:1/oversamp_freq:5;
input2=setpoint + sin(2*pi*oscillation_freq*t2)*oscillation_magnitude + randn(size(t2)) * variance;
output2=filter(b,a,input2);
plot(t2, [output2]);
xlabel("Time (seconds)");
ylabel("Value (degrees)");
print("oversamp");
