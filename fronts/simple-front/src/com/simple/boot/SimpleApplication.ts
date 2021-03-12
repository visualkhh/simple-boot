import {simstanceManager} from '@src/com/simple/boot/simstance/SimstanceManager'
import {ConstructorType} from '@src/com/simple/boot/types/Types'
import {Router} from '@src/com/simple/boot/module/Router'
import {fromEvent} from 'rxjs'
import {Renderer} from '@src/com/simple/boot/render/Renderer'

export class SimpleApplication {
    private routers: Router[] = []

    constructor(public sims: ConstructorType<any>[]) {
    }

    public run(): SimpleApplication {
        const sims = simstanceManager.getOrNewSims(Router);
        // console.log('------> sims', sims);
        // this.sims.map((it, i, a) => simstanceManager.resolve(it))
        this.routers = sims
            .filter(it => it instanceof Router)
            .map(it => it as Router)
            .sort((a, b) => a.path.length < b.path.length ? -1 : 1)
        console.log('simpleApplication--> ', this.routers, simstanceManager)
        this.routing()
        return this
    }

    private routing() {
        fromEvent(window, 'hashchange').subscribe((it) => {
            const executRouter = this.executeRouter()
            console.log('execute Router hashChange', executRouter)
        })
        window.dispatchEvent(new Event('hashchange'))
    }

    private executeRouter(): Router | undefined {
        console.log('executeRouter-->', this.routers)
        for (const router of this.routers) {
            if (router.hashchange('')) {
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
