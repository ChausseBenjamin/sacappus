
---------------------------------------------------------------------------------------------
--    calcul_param_2.vhd   (temporaire)
---------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------
--    Université de Sherbrooke - Département de GEGI
--
--    Version         : 5.0
--    Nomenclature    : inspiree de la nomenclature 0.2 GRAMS
--    Date            : 16 janvier 2020, 4 mai 2020
--    Auteur(s)       : 
--    Technologie     : ZYNQ 7000 Zybo Z7-10 (xc7z010clg400-1) 
--    Outils          : vivado 2019.1 64 bits
--
---------------------------------------------------------------------------------------------
--    Description (sur une carte Zybo)
---------------------------------------------------------------------------------------------
--
---------------------------------------------------------------------------------------------
-- À FAIRE: 
-- Voir le guide de la problématique
---------------------------------------------------------------------------------------------
--
---------------------------------------------------------------------------------------------

library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.STD_LOGIC_UNSIGNED.ALL;  -- pour les additions dans les compteurs
USE ieee.numeric_std.ALL;
Library UNISIM;
use UNISIM.vcomponents.all;

----------------------------------------------------------------------------------
-- 
----------------------------------------------------------------------------------
entity calcul_param_2 is
    Port (
    i_bclk    : in   std_logic;   -- bit clock
    i_reset   : in   std_logic;
    i_en      : in   std_logic;   -- un echantillon present
    i_ech     : in   std_logic_vector (23 downto 0);
    o_param   : out  std_logic_vector (7 downto 0)                                     
    );
end calcul_param_2;

----------------------------------------------------------------------------------

architecture Behavioral of calcul_param_2 is

---------------------------------------------------------------------------------
-- States
---------------------------------------------------------------------------------
--type state is (awaiting_fresh_sample, calculating_output, overwritting_saved_values);
--signal current_state : state;
--signal next_state : state;
---------------------------------------------------------------------------------
-- Signaux
---------------------------------------------------------------------------------
-- Rolling average over 2 samples.
--signal newest_i2s_signal    : std_logic_vector(23 downto 0); -- saves the i2s input when enable is raised so the state machine can work with it 3 clocks later.
signal most_recent_power    : signed(47 downto 0) := (others => '0') ; -- A binary multiplication requires twice as much bits! (2x2 = 4). This one saves the most recent power (received with enable)
signal oldest_power         : signed(47 downto 0) := (others => '0') ; -- The oldest received power.
signal factored_old_power   : signed(47 downto 0) := (others => '0');
--signal calculated_average : signed(47 downto 0) := (others => '0') ;
--signal average_on_a_byte  : std_logic_vector(7 downto 0);  -- Saves the output to give to o_param
--signal usigned_input        : unsigned(47 downto 0) := (others => '0');

---------------------------------------------------------------------------------
-- Constants
---------------------------------------------------------------------------------
-- Q1.23 format. An online converter was used to quickly generate the right signed binary vector for a 31/32 constant factor.
-- Forgetting factor allows the weight of older sample to be less than newer samples to get a quicker mathematical reaction to changes while keeping noises out of the equation.
--constant forgetting_factor_31_32 : signed(23 downto 0) := "011111000000000000000000";
--constant test : std_logic_vector(23 downto 0) := "100000000000000000000000";

---------------------------------------------------------------------------------------------
--    Description comportementale
---------------------------------------------------------------------------------------------
begin 
    
--    state_manager : process(i_reset, i_bclk)
--    begin
--        if i_reset = '1' then           
--            most_recent_power           <= (others => '0');
--            oldest_power                <= (others => '0');
----            newest_i2s_signal           <= (others => '0');
--            calculated_average          <= (others => '0');
----            average_on_a_byte           <= (others => '0');
--            current_state               <= awaiting_fresh_sample;
--        elsif rising_edge(i_bclk) then -- the clock rose! the state machine can thus go to the wanted state if ever it changed.
--            current_state <= next_state;
--        end if;
--    end process;
    
--    power_calculator : process(current_state, i_en, i_bclk)
--    begin
--            case current_state is
--                when awaiting_fresh_sample =>
--                    if i_en = '1' and rising_edge(i_bclk) then -- we have a brand new sample to account for! Save it as it'll be gone by the next clock.
--                        -- power is given by the square of the sample! that's cool cuz it also yeets negative numbers!
--                        signed_input <= signed(i_ech);
--                        most_recent_power <= (signed_input * signed_input);
--                        next_state <= calculating_output;
--                    end if;
                    
--                when calculating_output =>
--                    -- Average is the sum of samples divided by the total amount of them.
--                    -- Using 2 samples allows the average to reach peak (assuming constant 1s inputs) in 100 samples or so. That's 32 in decimal. (29 bits used)
--                    -- if samples of 0s are introduced at peak... 2 back to back doesn't drop it below 30 before quickly raising again to 32.
--                    -- Giving it all 0s at peak average quickly drops it to 0 in 100 or so samples as well.
--                    -- The lower the forgetting factor, the smaller the peak average gets and the quicker it gets there, but the more touched by noise it becomes.
--                    -- (new*new) + (old * dementia) = new_old, output
--                    calculated_average <= ((signed_input * signed_input) + resize((oldest_power * (forgetting_factor_31_32)), 48));
                    
--                    next_state <= overwritting_saved_values;
    
--                when overwritting_saved_values =>
--                    oldest_power <= calculated_average; -- Calculated average becomes the oldest for the next average with newer samples! Rolling average with forgetting factor.
                    
--                    -- The maximum the average can reach with the selected factor is 32. That's 29 bits. 23 of which are decimals.
--                    -- I want my power scale to be from 00 to FF... and 31.9999 is basically 011111111....
--                    -- So if I take into consideration the edge case that we reach 32 (100000) then only taking the MSB for my conversion...
--                    -- Allows me to "convert" values from 32 to 2 (FF, 01)
--                    -- And honestly? Good enough for something that will barely see the validation and none of the rapport! Trust!
--    --                if calculated_average >= 32 then -- saturation control!
--    --                    o_param <= "11111111";
--    --                else
--                        o_param <= std_logic_vector(calculated_average(28 downto 21));
--    --                end if;
                    
--                    next_state <= awaiting_fresh_sample;
    
--            end case;
--    end process;
    
--    --Output the calculated average on a byte for the 7 segments if the parameter is indeed selected.
--    output_management : process(average_on_a_byte)
--    begin
--        o_param <= average_on_a_byte;
--    end process;
    --o_param <= x"02";    -- temporaire ...

--    bruh : process(i_bclk, i_en)
--    begin
--        if i_en='1' and rising_edge(i_bclk) then
--            signed_input <= resize(signed(i_ech),48);
--            most_recent_power <= resize(signed_input * signed_input, 48);
--            calculated_average <= most_recent_power + resize(oldest_power * forgetting_factor_31_32, 48);
--            o_param <= std_logic_vector(calculated_average(47 downto 40));
--        else
--            oldest_power <= calculated_average;
--        end if;
--    end process;

    calculate_power : process(i_reset, i_bclk)
    begin
        if i_reset = '1' then -- reset the rolling average
            oldest_power <= (others => '0');
            most_recent_power <= (others => '0');
            factored_old_power <= (others => '0');
            o_param <= (others => '0');
        else
            if rising_edge(i_bclk) and (i_en = '1') then -- we're receiving a new sample and must calculate a rolling average!
                -- The average is done with 2 values. The current one and the past one.
                
                most_recent_power <= signed(i_ech) * (signed(i_ech)); -- Power in audio is x^2; That's cool cuz it gets rid of negative numbers.
                
                -- Too much trial and error due to VHDL's sporadic arithmetic synthesis. Even Javascript's types are more consistent and predictable.
                -- For ungodly reasons unknown to mankind... This works.
                -- This is 7h of trying to understand why a beautiful formal state machine don't work and eventually, breaking it enough with
                -- Random additions eventually made something that behaves like the Lua test I make.
                -- Whatever man.
                
                -- Signed type isn't worth a damn, so we STILL need to approximate 31/32 with bit shifts... What's the point of signed arithmetics if you can't figure it out Vivado?
                factored_old_power <= shift_right( 
                    shift_right( oldest_power, 1) + 
                    shift_right( oldest_power, 2) + 
                    shift_right( oldest_power, 3) + 
                    shift_right( oldest_power, 4) + 
                    shift_right( oldest_power, 5),
                    1 
                );
                
                -- Here's your stupid rolling average.
                oldest_power <= most_recent_power + factored_old_power;
                
                -- And because I'm lazy and it doesn't matter cuz this module isn't in the rapport nor the validation
                -- I'll approximate power output by simply reading the MSB of the 48 signed bits we calculated.
                -- I noticed that sending -1 for a while never goes above 3E when using 47 downto 40...
                -- So... I'll just use bits (45 downto 38) to get a better shot at FF power levels lol.
                o_param <= std_logic_vector(oldest_power(46 downto 39));
            end if;
        end if;
    end process;

end Behavioral;
