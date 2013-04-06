pkg load all; % Load the required signal processing toolbox

% sampling frequency in Hz
samp_freq = 1500;
oversamp_freq = 1500*16;
transition_band = oversamp_freq - (2 * samp_freq);

% nyquist frequency, half the sampling frequency
nyq_freq = samp_freq/2;
nyq_freq2 = oversamp_freq/2;

% cut off frequency, in Hz, between 0 and nyquist frequency
% try playing around and changing this value guys
cutoff_freq=2;

% create a first-order Butterworth low pass.
% The returned vectors are of legth n.
% Thus a first order filter is created with n = 2.
[b,a]=butter(2, cutoff_freq/nyq_freq);

% create a 5 seconds signal with 3 parts:
% a 1 Hz and a 200 Hz sine wave and some gaussian noise.
t=0:1/samp_freq:5;
input=sin(2*pi*t) + sin(2*pi*200*t) + randn(size(t));
output=filter(b,a,input);
plot(t, [input; output]);
print("normsamp");

% adjust butter sample to oversample frequencies
[b,a]=butter(2, cutoff_freq/nyq_freq2);

% oversample time interval
t2=0:1/oversamp_freq:5;
input2=sin(2*pi*t2) + sin(2*pi*200*t2) + randn(size(t2));
output2=filter(b,a,input2);
plot(t2, [input2; output2]);
print("oversamp");
