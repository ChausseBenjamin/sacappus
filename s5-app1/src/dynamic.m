clc
clear all
close all

global l_O l_1 l_2 m_A m_BA g alpha


l_O   = 0.50;   %% Base Shaft (m)
l_1   = 0.25;   %% First Arm  (OB) (m)
l_2   = 0.25;   %% Second Arm (BA) (m)
m_A   = 0.1;    %% Mass at point A (kg)
m_BA  = 1.0;    %% Mass of rod BA (kg)
g     = 9.81;   %% Gravitational acceleration (m/s^2)
alpha = 5.0;    %% Angular acceleration (rad/s^2)

%% X axis is phi (angle of rod BA)
phis = linspace(-pi/3, pi/3, 1000);

function result = Torque_B_z_dynamic(phi)
	global l_O l_1 l_2 m_A m_BA g alpha
	M_static = g * cos(phi) * l_2 * (m_A + m_BA/2);

	I_BA_center = (1/12) * m_BA * l_2^2;
	I_BA_B = I_BA_center + m_BA * (l_2/2)^2; % Total moment of inertia about B
	I_A = m_A * l_2^2; % Point mass at A

	M_dynamic = (I_BA_B + I_A) * alpha;

	result = M_static + M_dynamic;
end

% Pre-allocate arrays
Torque_values = zeros(size(phis));

% Compute values for each phi
for k = 1:length(phis)
    Torque_values(k) = Torque_B_z_dynamic(phis(k));
end

% Set gnuplot as graphics toolkit and force specific options
graphics_toolkit('gnuplot');

% Plot: Dynamic Torque M_B_z(phi)
figure(1)
plot(phis, Torque_values, '-', 'linewidth', 4, 'Color', '#629CFF')
grid on
xlabel('{/Symbol j} (rad)')
ylabel('M_{B_z} (N{/Symbol \267}m)')
axis tight
% Set figure size and position to minimize margins
set(gcf, 'PaperUnits', 'inches');
set(gcf, 'PaperSize', [6 4]);
set(gcf, 'PaperPosition', [0 0 6 4]);
print('rapport/figures/dynamic-torque.pdf', '-dpdf', '-color');

fprintf('\nPDF file has been generated with gnuplot:\n');
fprintf('- rapport/figures/dynamic-torque.pdf\n');

% Display some key values
fprintf('\nDynamic torque analysis:\n');
fprintf('At phi = -pi/3 rad (%.4f rad):\n', -pi/3);
fprintf('  M_B_z = %.6f N*m\n', Torque_B_z_dynamic(-pi/3));

fprintf('At phi = 0 rad:\n');
fprintf('  M_B_z = %.6f N*m\n', Torque_B_z_dynamic(0));

fprintf('At phi = pi/3 rad (%.4f rad):\n', pi/3);
fprintf('  M_B_z = %.6f N*m\n', Torque_B_z_dynamic(pi/3));

% Additional analysis
fprintf('\nSystem parameters used:\n');
fprintf('  l_0 = %.2f m\n', l_O);
fprintf('  l_1 = l_2 = %.2f m\n', l_1);
fprintf('  m_A = %.1f kg\n', m_A);
fprintf('  m_BA = %.1f kg\n', m_BA);
fprintf('  g = %.2f m/s^2\n', g);
fprintf('  alpha = %.1f rad/s^2\n', alpha);

fprintf('\nMaximum torque: %.6f N*m\n', max(Torque_values));
fprintf('Minimum torque: %.6f N*m\n', min(Torque_values));

% Calculate and display moment of inertia components
I_BA_center = (1/12) * m_BA * l_2^2;
I_BA_B = I_BA_center + m_BA * (l_2/2)^2;
I_A = m_A * l_2^2;
I_total = I_BA_B + I_A;

fprintf('\nMoment of inertia analysis:\n');
fprintf('  I_BA (about center): %.6f kg*m^2\n', I_BA_center);
fprintf('  I_BA (about B): %.6f kg*m^2\n', I_BA_B);
fprintf('  I_A (about B): %.6f kg*m^2\n', I_A);
fprintf('  I_total: %.6f kg*m^2\n', I_total);
fprintf('  Dynamic torque component: %.6f N*m\n', I_total * alpha);
