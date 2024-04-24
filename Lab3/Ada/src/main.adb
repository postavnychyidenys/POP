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
      ItemsProduced : Integer := 0;
      ItemsReceived : Integer := 0;


      task type ProducerTask;
      task body ProducerTask is
      begin

         while (ItemsProduced < ItemsTarget) loop

            Full_Storage.Seize;
            Access_Storage.Seize;

         if ItemsProduced >= ItemsTarget then
            Full_Storage.Release;
            Access_Storage.Release;
            Empty_Storage.Release;
            exit;
            end if;

            Storage.Append ("item " & ItemsProduced'Img);
            Put_Line ("Producer add item " & ItemsProduced'Img);
            ItemsProduced := ItemsProduced + 1;

            Access_Storage.Release;
            Empty_Storage.Release;

         end loop;
      end ProducerTask;


      task type ConsumerTask;
      task body ConsumerTask is
      begin

         while (ItemsReceived < ItemsTarget) loop

            Empty_Storage.Seize;
            Access_Storage.Seize;

            if ItemsReceived >= ItemsTarget then
            Full_Storage.Release;
            Access_Storage.Release;
            Empty_Storage.Release;

            exit;
            end if;
               Put_Line ("Consumer took " & First_Element (Storage));
            Storage.Delete_First;
            ItemsReceived := ItemsReceived + 1;

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
