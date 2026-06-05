clc
clear all
close all

global l_O l_1 l_2 omega
l_O   = 50; %% Base Shaft
l_1   = 25; %% First Arm  (OB)
l_2   = 25; %% Second Arm (BA)
omega = 25; %% Theta's speed (rad/s)

%% X axis is theta
thetas = linspace(0,pi/3,1000)

function result = A_y(theta)
	global l_O l_1 l_2 omega
	l = l_1
	result = l* ( sin(theta) + sin( acos(1-cos(theta)) ) )
end

function result = V_A_y(theta)
	global l_O l_1 l_2 omega
	l = l_1
	result = l*(cos(theta)*omega - cos( acos(1-cos(theta)) ) * (sin(theta)*omega/sin( acos(1-cos(theta)) )) )
end

% Pre-allocate arrays
A_y_values = zeros(size(thetas));
V_A_y_values = zeros(size(thetas));

% Compute values for each theta
for k = 1:length(thetas)
    A_y_values(k) = A_y(thetas(k));
    V_A_y_values(k) = V_A_y(thetas(k));
end

% Set gnuplot as graphics toolkit and force specific options
graphics_toolkit('gnuplot');

% Plot 1: Position A_y(theta)
figure(1)
plot(thetas, A_y_values, '-', 'linewidth', 4, 'Color', '#00BA38')
grid on
xlabel('{/Symbol q} (rad)')
ylabel('A_y (cm)')
axis tight
% Set figure size and position to minimize margins
set(gcf, 'PaperUnits', 'inches');
set(gcf, 'PaperSize', [6 4]);
set(gcf, 'PaperPosition', [0 0 6 4]);
print('rapport/figures/vertical-pos.pdf', '-dpdf', '-color');

% Plot 2: Velocity V_A_y(theta)
figure(2)
plot(thetas, V_A_y_values, '-', 'linewidth', 4, 'Color', '#629CFF')
grid on
xlabel('{/Symbol q} (rad)')
ylabel('V_{Ay} (cm/s)')
axis tight
set(gcf, 'PaperUnits', 'inches');
set(gcf, 'PaperSize', [6 4]);
set(gcf, 'PaperPosition', [0 0 6 4]);
print('rapport/figures/vertical-v.pdf', '-dpdf', '-color');

fprintf('\nPDF files have been generated with gnuplot:\n');
fprintf('- rapport/figures/vertical-pos.pdf\n');
fprintf('- rapport/figures/vertical-v.pdf\n');

% Display some key values
fprintf('At theta = 0 rad:\n');
fprintf('  A_y = %.4f m\n', A_y(0));
fprintf('  V_Ay = %.4f m/s\n', V_A_y(0));

fprintf('At theta = pi/3 rad:\n');
fprintf('  A_y = %.4f m\n', A_y(pi/3));
fprintf('  V_Ay = %.4f m/s\n', V_A_y(pi/3));
