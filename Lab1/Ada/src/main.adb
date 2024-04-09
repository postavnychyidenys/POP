with Ada.Text_IO;

procedure Main is

   thread_count : constant Integer := 5;
   can_stop : boolean := false;

   type Boolean_Array is array (Integer range <>) of Boolean;
   close_array : Boolean_Array(1..thread_count) := (others => False);
   type Int_Array is array (Integer range <>) of Integer;
   threads_id : Int_Array(1..thread_count);
   stop_time : Int_Array(1..thread_count) := (10, 5, 9, 4, 2);

   pragma Atomic(can_stop);

   task type break_thread is
      entry Start;
   end;
   task type main_thread is
      entry Start(id: in Integer; step: in Integer);
   end;

   threads : array(1..thread_count) of main_thread;

    procedure Swap(Arr: in out Int_Array; Index1, Index2: Integer) is
      Swap_Temp: Integer;
   begin
      Swap_Temp := Arr(Index1);
      Arr(Index1) := Arr(Index2);
      Arr(Index2) := Swap_Temp;
   end Swap;


   procedure Sort is
      swapped : Boolean := True;
   begin
      while swapped loop
         swapped := False;
         for I in 1 .. thread_count - 1 loop
            if stop_time(I) > stop_time(I + 1) then
               Swap(threads_id, I, I + 1);
               Swap(stop_time, I, I + 1);
               swapped := True;
            end if;
         end loop;
      end loop;
   end Sort;


   task body break_thread is
      sleep_time : Integer := 0;
   begin
      accept Start;

      for i in 1..thread_count loop
         sleep_time := stop_time(i);
         if i /= 1 then
            sleep_time := sleep_time - stop_time(i - 1);
         end if;

         Ada.Text_IO.Put_Line(sleep_time'Img);
         delay Duration(sleep_time);
         close_array(i) := True;
      end loop;

   end break_thread;

   task body main_thread is
      id : Integer;
      sum : Long_Long_Integer := 0;
      step : Integer;
   begin
      accept Start (id: in Integer; step: in Integer) do
         main_thread.id := id;
         main_thread.step := step;
      end Start;

      loop
         sum := sum + Long_Long_Integer(step);
         exit when close_array(id);
      end loop;

      Ada.Text_IO.Put_Line(sum'Img);
   end main_thread;

   break : break_thread;

begin
    for i in 1..thread_count loop
      threads(i).Start(i, i * 4);
      threads_id(i) := i;
   end loop;
   Sort;
   break.Start;
end Main;
