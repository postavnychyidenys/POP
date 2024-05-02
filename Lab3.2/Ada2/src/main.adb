with Ada.Text_IO, GNAT.Semaphores;
use Ada.Text_IO, GNAT.Semaphores;
with Ada.Containers.Indefinite_Doubly_Linked_Lists;
use Ada.Containers;
with GNAT.Threads;


procedure Main is

   package String_Lists is new Indefinite_Doubly_Linked_Lists (String);
   use String_Lists;

   procedure Init (ItemsTarget : in Integer; StorageSize : in Integer;
                   ProducersCount : in Integer; ConsumersCount : in Integer) is
      Storage : List;
      Access_Storage : Counting_Semaphore (1, Default_Ceiling);
      Full_Storage   : Counting_Semaphore (StorageSize, Default_Ceiling);
      Empty_Storage  : Counting_Semaphore (0, Default_Ceiling);
      ItemsProduced : Integer := ItemsTarget;
      ItemsReceived : Integer := ItemsTarget;

      procedure GetItemsProduced(Result : out Boolean) is
      begin
         Result := ItemsProduced = 0;
      end GetItemsProduced;

      procedure GetItemsReceived(Result : out Boolean) is
      begin
         Result := ItemsReceived = 0;
      end GetItemsReceived;

      procedure DecrementProduced is
      begin
         if ItemsProduced > 0 then
            ItemsProduced := ItemsProduced - 1;
         end if;
      end DecrementProduced;

      procedure DecrementReceived is
      begin
         if ItemsReceived > 0 then
            ItemsReceived := ItemsReceived - 1;
         end if;
      end DecrementReceived;


      function IsProductionDone return Boolean is
      begin
      declare
         Result : Boolean := False;
      begin
         GetItemsProduced(Result);
         DecrementProduced;
         return Result;
      end;
      end IsProductionDone;

       function IsReceivedDone return Boolean is
       begin
       declare
         Result : Boolean := False;
       begin
         GetItemsReceived(Result);
         DecrementReceived;
         return Result;
       end;
       end IsReceivedDone;

      task type ProducerTask;
      task body ProducerTask is
      begin

         while not IsProductionDone loop

            Full_Storage.Seize;
            Access_Storage.Seize;

            Storage.Append ("item ");
            Put_Line ("Producer add item");

            Access_Storage.Release;
            Empty_Storage.Release;

         end loop;
      end ProducerTask;


      task type ConsumerTask;
      task body ConsumerTask is
      begin


         while not IsReceivedDone loop

            Empty_Storage.Seize;
            Access_Storage.Seize;

            Put_Line ("Consumer took " & First_Element (Storage));
            Storage.Delete_First;

            Access_Storage.Release;
            Full_Storage.Release;

         end loop;
      end ConsumerTask;

      Consumers : Array(1..ConsumersCount) of ConsumerTask;
      Producers : Array(1..ProducersCount) of ProducerTask;

   begin
      null;
   end Init;

begin
   Init(10, 3, 6, 4);
end Main;
