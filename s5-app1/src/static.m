clc
clear all
close all

global l_O l_1 l_2 m_A m_BA g

% Given parameters - converted to SI units
l_O   = 0.50;   %% Base Shaft (m)
l_1   = 0.25;   %% First Arm  (OB) (m)
l_2   = 0.25;   %% Second Arm (BA) (m)
m_A   = 0.1;    %% Mass at point A (kg)
m_BA  = 1.0;    %% Mass of rod BA (kg)
g     = 9.81;   %% Gravitational acceleration (m/s^2)

%% X axis is phi (angle of rod BA)
phis = linspace(-pi/3, pi/3, 1000);

function result = Torque_B_z(phi)
	global l_O l_1 l_2 m_A m_BA g
	result = g * cos(phi) * l_2 * (m_A + m_BA/2);
end

% Pre-allocate arrays
Torque_values = zeros(size(phis));

% Compute values for each phi
for k = 1:length(phis)
    Torque_values(k) = Torque_B_z(phis(k));
end

% Set gnuplot as graphics toolkit and force specific options
graphics_toolkit('gnuplot');

% Plot: Torque M_B_z(phi)
figure(1)
plot(phis, Torque_values, '-', 'linewidth', 4, 'Color', '#00BA38')
grid on
xlabel('{/Symbol j} (rad)')
ylabel('M_{B_z} (N{/Symbol \267}m)')
axis tight
% Set figure size and position to minimize margins
set(gcf, 'PaperUnits', 'inches');
set(gcf, 'PaperSize', [6 4]);
set(gcf, 'PaperPosition', [0 0 6 4]);
print('../rapport/figures/static-torque.pdf', '-dpdf', '-color');

fprintf('\nPDF file has been generated with gnuplot:\n');
fprintf('- ../rapport/figures/static-torque.pdf\n');

% Display some key values
fprintf('\nStatic torque analysis:\n');
fprintf('At phi = -pi/3 rad (%.4f rad):\n', -pi/3);
fprintf('  M_B_z = %.6f N*m\n', Torque_B_z(-pi/3));

fprintf('At phi = 0 rad:\n');
fprintf('  M_B_z = %.6f N*m\n', Torque_B_z(0));

fprintf('At phi = pi/3 rad (%.4f rad):\n', pi/3);
fprintf('  M_B_z = %.6f N*m\n', Torque_B_z(pi/3));

% Additional analysis
fprintf('\nSystem parameters used:\n');
fprintf('  l_0 = %.2f m\n', l_O);
fprintf('  l_1 = l_2 = %.2f m\n', l_1);
fprintf('  m_A = %.1f kg\n', m_A);
fprintf('  m_BA = %.1f kg\n', m_BA);
fprintf('  g = %.2f m/s^2\n', g);

fprintf('\nMaximum torque: %.6f N*m (at phi = 0)\n', max(Torque_values));
fprintf('Minimum torque: %.6f N*m (at phi = ±pi/3)\n', min(Torque_values));
