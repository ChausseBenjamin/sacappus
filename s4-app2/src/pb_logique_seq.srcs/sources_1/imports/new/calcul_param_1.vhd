
---------------------------------------------------------------------------------------------
--    calcul_param_1.vhd
---------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------
--    Universit� de Sherbrooke - D�partement de GEGI
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
-- � FAIRE:
-- Voir le guide de la probl�matique
---------------------------------------------------------------------------------------------
--
---------------------------------------------------------------------------------------------

library ieee;
use ieee.std_logic_1164.all;

use ieee.std_logic_unsigned.all;  -- pour les additions dans les compteurs
use ieee.numeric_std.all;
library unisim;
use unisim.vcomponents.all;

----------------------------------------------------------------------------------
--
----------------------------------------------------------------------------------
entity calcul_param_1 is Port (
  i_bclk  : in  std_logic; -- bit clock (I2S)
  i_reset : in  std_logic;
  i_en    : in  std_logic; -- un echantillon present a l'entr�e
  i_ech   : in  std_logic_vector (23 downto 0); -- echantillon en entr�e
  o_param : out std_logic_vector (7 downto 0)); -- param�tre calcul�
end calcul_param_1;

----------------------------------------------------------------------------------

architecture Behavioral of calcul_param_1 is

  type state is (await_sample, overwrite_cache, calc_avg, check_sign_change, update_freq);
  signal current_state        : state := await_sample;
  signal next_state           : state := await_sample;

  constant sample_rate        : integer := 48000;

--  signal cache_0              : signed(23 downto 0):=(others => '0');
--  signal cache_1              : signed(23 downto 0):=(others => '0');
--  signal cache_2              : signed(23 downto 0):=(others => '0');
--  signal cache_3              : signed(23 downto 0):=(others => '0');

  signal write_counter        : integer := 0;
  signal since_last_zero      : integer := 0;
  signal since_2nd_last_zero  : integer := 0;
  signal freq_est             : integer := 0;
  signal freq_est_unsigned    : unsigned (7 downto 0);
  signal current_sample       : signed(23 downto 0):=(others => '0');
  signal averaged_sample      : signed(23 downto 0):=(others => '0');
  signal cache_to_overwrite   : unsigned(1 downto 0):="00";

  signal cache_sum            : signed(25 downto 0):=(others => '0');
  signal cache_avg            : signed(23 downto 0):=(others => '0');

  signal prev_sample_was_neg  : std_logic:='0';

---------------------------------------------------------------------------------------------
--    Description comportementale
---------------------------------------------------------------------------------------------
begin


  sequential : process(i_reset, i_bclk)
  begin
    if i_reset = '1' then -- processes must stop and be reset.
      current_state <= await_sample;
--      cache_0 <= (others => '0');
--      cache_1 <= (others => '0');
--      cache_2 <= (others => '0');
--      cache_3 <= (others => '0');

    elsif rising_edge(i_bclk) then -- we go to the next state at each clock front.
      -- Set the current state as the wanted state (MEF state management)
      current_state <= next_state;
    end if;
  end process;

  state_machine : process(current_state, i_en)
  
    variable cache_0              : signed(23 downto 0):=(others => '0');
    variable cache_1              : signed(23 downto 0):=(others => '0');
    variable cache_2              : signed(23 downto 0):=(others => '0');
    variable cache_3              : signed(23 downto 0):=(others => '0');
  
  begin
    --if rising_edge(i_bclk) then
    case current_state is
      when await_sample =>
        if i_en = '1' then
          current_sample <= signed(i_ech);
          since_last_zero <= since_last_zero+1;
          since_2nd_last_zero <= since_2nd_last_zero+1;
          next_state <= overwrite_cache;
        --else
        --  next_state <= await_sample;
        end if;

      when overwrite_cache =>
        case cache_to_overwrite is
          when "00" =>
            cache_0 := current_sample;
          when "01" =>
            cache_1 := current_sample;
          when "10" =>
            cache_2 := current_sample;
          when "11" =>
            cache_3 := current_sample;
          when others =>
            -- SHOULD NOT HAPPEN...
            cache_0 := current_sample;
        end case;
        cache_to_overwrite <= cache_to_overwrite+1;
        next_state <= calc_avg;

      when calc_avg =>

        cache_sum <= resize(cache_0, 26) + resize(cache_1, 25)
                   + resize(cache_2, 25) + resize(cache_3, 25);
        cache_avg <= cache_sum(25 downto 2); -- Bitshift boiii

        next_state <= check_sign_change;

      when check_sign_change =>
        case prev_sample_was_neg is
          when '1' =>
            if (cache_avg > 0) then
              prev_sample_was_neg <= not prev_sample_was_neg;
              next_state <= update_freq;
            else
              next_state <= await_sample;
            end if;
          when '0' =>
            if (cache_avg < 0) then
              prev_sample_was_neg <= not prev_sample_was_neg;
              next_state <= update_freq;
            else
              next_state <= await_sample;
            end if;
          when others =>
          -- SHOULD NOT HAPPEN...
          next_state <= await_sample;
      end case;

      when update_freq =>
        -- freq_est <= sample_rate / since_2nd_last_zero;
        -- freq_est_unsigned <= to_unsigned(freq_est, 8);
        o_param <= std_logic_vector(to_unsigned(since_2nd_last_zero,8));
        since_2nd_last_zero <= since_last_zero;
        since_last_zero <= 0;
        next_state <= await_sample;

      when others =>
        -- SHOULD NOT HAPPEN...
        next_state <= await_sample;

    end case;
    --end if;
  end process;



end Behavioral;
