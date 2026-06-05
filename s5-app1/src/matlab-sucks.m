clc
clear all
close all

global l_O l_1 l_2
l_O = 50; %% Base Shaft
l_1 = 25; %% First Arm  (OB)
l_2 = 25; %% Second Arm (BA)

function result = pos_O()
	global l_O
	%%        X  Y    Z
	result = [0, l_O, 0];
end

function result = pos_B(theta)
	global l_O l_1 l_2
	result = [
		l_1 * cos(theta),           %% X
		l_O + ( l_1 * sin(theta) ), %% Y
		0                           %% Z
	];
end

function result = pos_A(theta, phi)
	global l_O l_1 l_2
	result = [
		l_1 * cos(theta) + l_2*cos(theta+phi),     %% X
		l_O + l_1*sin(theta) + l_2*sin(theta+phi), %% Y
		0                                          %% Z
	];
end

disp("O")
disp(pos_O())
disp("B")
disp(pos_B(0))
disp("A")
disp(pos_A(0,pi/2))

%% Plot how theta motion affects the A point in X,Y
%% with a constant phi of pi/2
phi = pi/2

dt = 1e-3
thetas = [0:dt:pi/3]

% Pre-allocate the vector for A positions
A = zeros(length(thetas), 3);

% Compute A positions
for k = 1:length(thetas)
    A(k,:) = pos_A(thetas(k), phi);
end

% 3D plot: theta vs X and Y
figure
plot3(thetas, A(:,1), A(:,2), 'LineWidth', 2)
grid on
xlabel('\theta (rad)')
ylabel('X')
zlabel('Y')
title('Trajectory of point A: theta vs (X,Y)')
view(45,30)
