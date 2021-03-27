import {simstanceManager} from '@src/com/simple/boot/simstance/SimstanceManager'
import {ConstructorType} from '@src/com/simple/boot/types/Types'
import {Router} from '@src/com/simple/boot/module/Router'
import {fromEvent} from 'rxjs'
import {Renderer} from '@src/com/simple/boot/render/Renderer'

export class SimpleApplication {
    private routers: Router[] = []

    constructor(public sims: ConstructorType<any>[]) {
    }

    public async run(): Promise<SimpleApplication> {
        const sims = simstanceManager.getOrNewSims(Router);
        this.routers = sims
            .map(it => it as Router)
            .sort((a, b) => a.path.length < b.path.length ? -1 : 1)
        this.startRouting()
        return this
    }

    private startRouting() {
        fromEvent(window, 'hashchange').subscribe((it) => this.executeRouter())
        window.dispatchEvent(new Event('hashchange'))
    }

    private executeRouter(): Router | undefined {
        console.log('executeRouter-->', this.routers)
        for (const router of this.routers) {
            if (router.hashchange([''])) {
                return router
            } else {
                Renderer.render('404 not found')
            }
        }
    }

    public getSim<T>(key: ConstructorType<T>): T {
        return simstanceManager.getOrNewSim(key)
    }
}
