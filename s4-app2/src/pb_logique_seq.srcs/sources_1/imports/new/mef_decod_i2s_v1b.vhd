---------------------------------------------------------------------------------------------
-- circuit mef_decod_i2s_v1b.vhd                   Version mise en oeuvre avec des compteurs
---------------------------------------------------------------------------------------------
-- Université de Sherbrooke - Département de GEGI
-- Version         : 1.0
-- Nomenclature    : 0.8 GRAMS
-- Date            : 7 mai 2019
-- Auteur(s)       : Daniel Dalle
-- Technologies    : FPGA Zynq (carte ZYBO Z7-10 ZYBO Z7-20)
--
-- Outils          : vivado 2019.1
---------------------------------------------------------------------------------------------
-- Description:
-- MEF pour decodeur I2S version 1b
-- La MEF est substituee par un compteur
--
-- notes
-- frequences (peuvent varier un peu selon les contraintes de mise en oeuvre)
-- i_lrc        ~ 48.    KHz    (~ 20.8    us)
-- d_ac_mclk,   ~ 12.288 MHz    (~ 80,715  ns) (non utilisee dans le codeur)
-- i_bclk       ~ 3,10   MHz    (~ 322,857 ns) freq mclk/4
-- La durée d'une période reclrc est de 64,5 périodes de bclk ...
--
-- Revision  
-- Revision 14 mai 2019 (version ..._v1b) composants dans entités et fichiers distincts
---------------------------------------------------------------------------------------------
-- À faire :
--
--
---------------------------------------------------------------------------------------------

library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.STD_LOGIC_UNSIGNED.ALL;  -- pour les additions dans les compteurs

entity mef_decod_i2s_v1b is
   Port ( 
   i_bclk      : in std_logic;
   i_reset     : in    std_logic; 
   i_lrc       : in std_logic;
   i_cpt_bits  : in std_logic_vector(6 downto 0);   -- says we're saving the nth bit of 24 bits i2s word
 --  
   o_bit_enable     : out std_logic ;  -- Enables the counter and the shift register
   o_load_left      : out std_logic ;  -- Makes the left register save the data of the shift register
   o_load_right     : out std_logic ;  -- Makes the right register save the data of the shift register
   o_str_dat        : out std_logic ;  -- 
   o_cpt_bit_reset  : out std_logic    -- Resets the counter 
   
);
end mef_decod_i2s_v1b;

architecture Behavioral of mef_decod_i2s_v1b is

    -- States of the state machine
    type state is (awaiting_lrc_front_change, reading_24bit_word, saving_received_word, setting_str_dat);
    signal current_state : state;
    signal next_state : state;
    
    -- front change detection signals
    signal lrc_front_changed : std_logic; -- '1' = the value of i_lrc changed. A new word is thus comming in the next clocks.
    signal lrc_saved_front   : std_logic; --  saves what lrc used to be, to detect a front change.
--    signal d_cpt_bit_reset   : std_logic; -- because the reset signal and the state machine needs to have access to the reset output.

    signal   d_reclrc_prec  : std_logic ;  --
    
begin

    ---------------------------------------------
    -- Sequential logic managing
    ---------------------------------------------
    sequential : process(i_reset, i_bclk)
    begin
        if i_reset = '1' then -- processes must stop and be reset.
            current_state <= awaiting_lrc_front_change;
            lrc_front_changed <= '0';
            lrc_saved_front <= '0';

        elsif rising_edge(i_bclk) then -- we go to the next state at each clock front.
            -- Set the current state as the wanted state (MEF state management)
            current_state <= next_state;
            -- Check for raising or falling edge on the I2S word select (lrc)
            if i_lrc /= lrc_saved_front then -- the front changed!
                lrc_saved_front <= i_lrc; -- save the lrc so the next comparison don't trigger a front change event!
                lrc_front_changed <= '1';
            else
                lrc_front_changed <= '0';
            end if;
        end if;
    end process;

    ---------------------------------------------
    -- Combinatorial logic managing (I2S decoding)
    ---------------------------------------------
    mef_state_management : process(current_state, lrc_front_changed, i_cpt_bits)
    begin
        case current_state is
            when awaiting_lrc_front_change =>
                -- Outputs
                o_load_right <= '0';
                o_load_left <= '0';
                o_str_dat <= '0';
                o_cpt_bit_reset <= not lrc_front_changed; -- if it's lowered just at the next clock event, then a bit won't get read.
                o_bit_enable <= lrc_front_changed;        -- This allows the variable change to have immediate effect on outputs.
                
                -- Handling the next state
                if lrc_front_changed = '1' then -- the value of i_lrc changed since the last clock pulse! That means the bits at the next clock must be read!
                    next_state <= reading_24bit_word;
                else
                    next_state <= awaiting_lrc_front_change;
                end if;
                
            when reading_24bit_word =>
                -- Outputs
                o_load_right <= '0';
                o_load_left <= '0';
                o_bit_enable <= '1'; -- shift register and counters are online! Saving and counting serial I2S bits!
                o_cpt_bit_reset <= '0';
                o_str_dat <= '0';
                
                -- Handling the next state
                if i_cpt_bits > 22 then -- at this clock cycle, we saved the 23th bit (0 being the first). The next bit must NOT be saved!
                    next_state <= saving_received_word;
                else -- We've still got valid bits incoming!
                    next_state <= reading_24bit_word;
                end if;

            when saving_received_word =>
                -- Outputs
                o_load_right <= i_lrc;      -- right channel is received when the word select is high.
                o_load_left <= not i_lrc;   -- left channel is received when the word select is low.
                o_bit_enable <= '0';
                o_cpt_bit_reset <= '0';
                o_str_dat <= '0';
                
                -- This state lasts for one clock pulse. So we pulse the parallel registers for a clock cycle so they
                -- can save the data from the shift register, then we output i_lrc on str_dat to be conform with the old state machine.
                next_state <= setting_str_dat;
                
            when setting_str_dat =>
                -- Outputs
                o_load_right <= '0';
                o_load_left <= '0';
                o_bit_enable <= '0';
                o_cpt_bit_reset <= '0';
                o_str_dat <= i_lrc;     -- Step to conform with the old state mach9ine. Important as it's tied to parameter enables in the schema block.
                
                -- Go back to awaiting a lrc value change
                next_state <= awaiting_lrc_front_change;

        end case;
    end process;
    
    ---------------------------------------------
    -- Old code (kept for tests and developments)
    ---------------------------------------------
   -- pour detecter transitions d_ac_reclrc
--   reglrc_I2S: process ( i_bclk)
--   begin
--   if i_bclk'event and (i_bclk = '1') then
--        d_reclrc_prec <= i_lrc;
--   end if;
--   end process;
   
--   -- synch compteur codeur
--   rest_cpt: process (i_lrc, d_reclrc_prec, i_reset)
--      begin
--         o_cpt_bit_reset <= (d_reclrc_prec xor i_lrc) or i_reset;
--      end process;
      

--      -- decodage compteur avec case ...   
--        sig_ctrl_I2S:  process (i_cpt_bits, i_lrc )
--            begin
--                case i_cpt_bits is
--                 when "0000000" =>
--                     o_bit_enable     <= '1';
--                     o_load_left      <= '0';
--                     o_load_right     <= '0';
--                     o_str_dat        <= '0';
--                 when   "0000001"  |  "0000010"  |  "0000011"  |  "0000100"  
--                       |  "0000101"  |  "0000110"  |  "0000111"  |  "0001000" 
--                       |  "0001001"  |  "0001010"  |  "0001011"  |  "0001100" 
--                       |  "0001101"  |  "0001110"  |  "0001111"  |  "0010000"  
--                       |  "0010001"  |  "0010010"  |  "0010011"  |  "0010100" 
--                       |  "0010101"  |  "0010110"  |  "0010111"   
--                    =>
--                     o_bit_enable     <= '1';
--                     o_load_left      <= '0';
--                     o_load_right     <= '0';
--                     o_str_dat        <= '0';
--                 when   "0011000"  =>
--                     o_bit_enable     <= '0';
--                     o_load_left      <= not i_lrc;
--                     o_load_right     <=  i_lrc;
--                     o_str_dat        <= '0';
--                 when    "0011001"  =>
--                    o_bit_enable     <= '0';
--                    o_load_left     <= '0';
--                    o_load_right     <= '0';
--                    o_str_dat        <=  i_lrc;
--                 when  others  =>
--                    o_bit_enable     <= '0';
--                    o_load_left      <= '0';
--                    o_load_right     <= '0';
--                    o_str_dat        <= '0';
--                 end case;
--             end process;

     end Behavioral;