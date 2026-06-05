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

function result = A_x(theta)
	global l_O l_1 l_2 omega
	l = l_1
	result = l*(cos(theta) + cos( -1 * theta ))
end

function result = V_A_x(theta)
	global l_O l_1 l_2 omega
	l = l_1
	result = -2*l*sin(theta)*omega
end

function result = Alpha_A_x(theta)
	global l_O l_1 l_2 omega
	l = l_1
	result = -2*l*(omega^2)*cos(theta)
end

% Define theta range from 0 to pi/3 radian
dt = 1e-3;

% Pre-allocate arrays
A_x_values = zeros(size(thetas));
V_A_x_values = zeros(size(thetas));
Alpha_A_x_values = zeros(size(thetas));

% Compute values for each theta
for k = 1:length(thetas)
    A_x_values(k) = A_x(thetas(k));
    V_A_x_values(k) = V_A_x(thetas(k));
    Alpha_A_x_values(k) = Alpha_A_x(thetas(k));
end

% Set gnuplot as graphics toolkit and force specific options
graphics_toolkit('gnuplot');

% Plot 1: Position A_x(theta)
figure(1)
plot(thetas, A_x_values, '-', 'linewidth', 4, 'Color', '#00BA38')
grid on
xlabel('{/Symbol q} (rad)')
ylabel('A_x (cm)')
axis tight
% Set figure size and position to minimize margins
set(gcf, 'PaperUnits', 'inches');
set(gcf, 'PaperSize', [6 4]);
set(gcf, 'PaperPosition', [0 0 6 4]);
print('rapport/figures/horizontal-pos.pdf', '-dpdf', '-color');

% Plot 2: Velocity V_A_x(theta)
figure(2)
plot(thetas, V_A_x_values, '-', 'linewidth', 4, 'Color', '#629CFF')
grid on
xlabel('{/Symbol q} (rad)')
ylabel('V_{Ax} (cm/s)')
axis tight
set(gcf, 'PaperUnits', 'inches');
set(gcf, 'PaperSize', [6 4]);
set(gcf, 'PaperPosition', [0 0 6 4]);
print('rapport/figures/horizontal-v.pdf', '-dpdf', '-color');

% Plot 3: Acceleration Alpha_A_x(theta)
figure(3)
plot(thetas, Alpha_A_x_values, '-', 'linewidth', 4, 'Color', '#F8766D')
grid on
xlabel('{/Symbol q} (rad)')
ylabel('{/Symbol a}_{Ax} (cm/s^2)')
axis tight
set(gcf, 'PaperUnits', 'inches');
set(gcf, 'PaperSize', [6 4]);
set(gcf, 'PaperPosition', [0 0 6 4]);
print('rapport/figures/horizontal-alpha.pdf', '-dpdf', '-color');

fprintf('\nPDF files have been generated with gnuplot:\n');
fprintf('- rapport/figures/horizontal-pos.pdf\n');
fprintf('- rapport/figures/horizontal-v.pdf\n');
fprintf('- rapport/figures/horizontal-alpha.pdf\n');

% Display some key values
fprintf('At theta = 0 rad:\n');
fprintf('  A_x = %.4f m\n', A_x(0));
fprintf('  V_Ax = %.4f m/s\n', V_A_x(0));
fprintf('  Alpha_Ax = %.4f m/s^2\n', Alpha_A_x(0));

fprintf('At theta = pi/3 rad:\n');
fprintf('  A_x = %.4f m\n', A_x(pi/3));
fprintf('  V_Ax = %.4f m/s\n', V_A_x(pi/3));
fprintf('  Alpha_Ax = %.4f m/s^2\n', Alpha_A_x(pi/3));
