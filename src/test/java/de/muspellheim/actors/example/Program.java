package de.muspellheim.actors.example;

import de.muspellheim.actors.example.actors.AlarmbellActor;
import de.muspellheim.actors.example.actors.DlgActor;
import de.muspellheim.actors.example.actors.WatchdogActor;
import de.muspellheim.actors.example.actors.messages.CurrentTimeEvent;
import de.muspellheim.actors.example.portals.Clock;
import de.muspellheim.actors.example.portals.DlgAlarmclock;
import de.muspellheim.actors.example.providers.Alarmbell;

public class Program {

    public static void main(String args[]) {
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> e.printStackTrace());

        Alarmbell bell = new Alarmbell();
        Clock clock = new Clock();
        Watchdog dog = new Watchdog();
        DlgAlarmclock dlg = new DlgAlarmclock();

        AlarmbellActor bellActor = new AlarmbellActor(bell);
        WatchdogActor dogActor = new WatchdogActor(dog);
        DlgActor dlgActor = new DlgActor(dlg);

        dlgActor.event.addHandler(e -> dogActor.receive(e));
        dogActor.event.addHandler(e -> dlgActor.receive(e));
        dogActor.event.addHandler(e -> bellActor.receive(e));

        clock.onCurrentTime.addHandler(t -> {
            CurrentTimeEvent e = new CurrentTimeEvent();
            e.currentTime = t;
            dlgActor.receive(e);
            dogActor.receive(e);
        });
    }

}
