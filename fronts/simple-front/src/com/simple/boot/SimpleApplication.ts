import {SimstanceManager} from '@src/com/simple/boot/simstance/SimstanceManager'
import {ConstructorType} from '@src/com/simple/boot/types/Types'
import {Router} from '@src/com/simple/boot/module/Router'
import {fromEvent} from 'rxjs'
import {fromIterable} from 'rxjs/internal-compatibility'
import {filter, map, toArray} from 'rxjs/operators'

export class SimpleApplication {
    constructor(public sims: ConstructorType<any>[]) {
    }

    public run(): SimpleApplication {
        this.sims.map((it, i, a) => SimstanceManager.resolve(it))
        this.routing();
        return this;
    }

    private async routing() {
        const routers = await fromIterable(SimstanceManager.storege.values()).pipe(
            filter(it => it instanceof Router),
            map(it => it as Router),
            toArray()
        ).toPromise();

        routers.forEach(it => it.hashchange())

        fromEvent(window, 'hashchange').subscribe((it) => {
            const nEvent = it as HashChangeEvent;
            routers.forEach(it => it.hashchange(nEvent))
        })
    }

    public getSim<T>(key: ConstructorType<T>): T {
        return SimstanceManager.getSim(key);
    }
}
