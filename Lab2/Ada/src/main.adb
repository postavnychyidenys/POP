with Ada.Text_IO; use Ada.Text_IO;
with Ada.Numerics.discrete_Random;

procedure main is

   dim : constant integer := 100000;
   threadCount : constant integer := 5;
   arr : array(1..dim) of integer;


   function generateRandomNumber (from: in integer; to: in integer) return integer is
       subtype Rand_Range is integer range from .. to;
       package Rand_Int is new Ada.Numerics.Discrete_Random(Rand_Range);
       use Rand_Int;
       gen : Rand_Int.Generator;
       generateNum: Rand_Range;
   begin
      Rand_Int.Reset(gen);
      generateNum := Random(gen);
      return generateNum;
   end;

   procedure InitArray is
      rndIndex : integer;
      rndValue : integer;
   begin
      for i in 1..dim loop
         arr(i) := 0;
      end loop;

      rndIndex := generateRandomNumber(0, dim);
      rndValue := generateRandomNumber(-100000, 0);
      arr(rndIndex) := rndValue;

      Put_Line("");
      Put_Line("Random generated index is" & rndIndex'img & ", random generated value is" & rndValue'img);
   end InitArray;

   task type Thread is
      entry Init(threadIndex : in integer);
   end Thread;

   protected ThreadManager is
      procedure AddStopThread(MinIndex : in integer; MinValue : in integer; ThreadIndex : in integer);
      entry GetMinIndexAndValue(MinIndex : out integer; MinValue : out integer);
   private
      min_Index : integer;
      min_Value : integer;
      flag : Boolean := true;
      stopThreadCount : integer;
   end ThreadManager;

   protected body ThreadManager is
      procedure AddStopThread(MinIndex : in integer; MinValue : in integer; ThreadIndex : in integer) is
      begin
         Put_Line("");
         Put_Line("Min element in thread" & ThreadIndex'img & " with index" & MinIndex'img & " is" & MinValue'img);

         if (flag) then
            min_Value := MinValue;
            min_Index := MinIndex;
            flag := false;
         else
            if (MinValue < min_Value) then
               min_Value := MinValue;
               min_Index := MinIndex;
            end if;
         end if;
         stopThreadCount := stopThreadCount + 1;
      end AddStopThread;

      entry GetMinIndexAndValue(MinIndex : out Integer; MinValue : out Integer) when stopThreadCount = threadCount is
      begin
         MinIndex := min_Index;
         MinValue := min_Value;
      end GetMinIndexAndValue;
   end ThreadManager;

   task body Thread is
      minIndex : integer;
      minValue : integer;
      startIndex, finishIndex : integer;
      threadIndex : integer;
   begin
      accept Init(threadIndex : in integer) do
         Thread.threadIndex := threadIndex;
      end Init;

      startIndex := ((threadIndex - 1) * dim / threadCount) + 1;
      finishIndex := threadIndex * dim / threadCount;
      minIndex := startIndex;
      minValue := arr(minIndex);

      for i in startIndex..finishIndex loop
         if (arr(i) < minValue) then
            minIndex := i;
            minValue := arr(i);
         end if;
      end loop;
      ThreadManager.AddStopThread(minIndex, minValue, threadIndex);
   end Thread;

   threads : array(1..threadCount) of Thread;

   procedure PrintResults is
      minIndex : integer;
      minValue : integer;
   begin
      ThreadManager.GetMinIndexAndValue(minIndex, minValue);

      Put_Line("");
      Put("Min index is" & minIndex'img & ", min value is" & minValue'img);
   end PrintResults;

begin
   Put_Line("Threads count is" & threadCount'img);

   InitArray;
   for i in 1..threadCount loop
      threads(i).Init(i);
   end loop;

   PrintResults;
end main;
