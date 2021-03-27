import {simstanceManager} from '@src/com/simple/boot/simstance/SimstanceManager'
import {ConstructorType} from '@src/com/simple/boot/types/Types'
import {Router} from '@src/com/simple/boot/module/Router'
import {fromEvent} from 'rxjs'
import {Renderer} from '@src/com/simple/boot/render/Renderer'
import {Module} from '@src/com/simple/boot/module/Module'

export class SimpleApplication {
    // private routers: Router[] = []
    // constructor(public sims: ConstructorType<any>[]) {
    constructor(public rootRouter: ConstructorType<Router>) {
    }

    public async run(): Promise<SimpleApplication> {
        // const sims = simstanceManager.getOrNewSims(Router);
        // this.routers = sims
        //     .map(it => it as Router)
        //     .sort((a, b) => a.path.length < b.path.length ? -1 : 1)
        this.startRouting()
        return this
    }

    private startRouting() {
        fromEvent(window, 'hashchange').subscribe((it) => this.executeRouter())
        window.dispatchEvent(new Event('hashchange'))
    }

    private executeRouter() {
        const routers: Router[] = [];
        const executeModule = simstanceManager.getOrNewSim(this.rootRouter)?.getExecuteModule(routers)
        if (executeModule) {
            console.log('executeRouter-->', routers, executeModule)
            let lastRouterSelector = 'app';
            routers.forEach(it => {
                this.renderRouterModule(it.moduleObject, lastRouterSelector);
                const selctor = it?.moduleObject?.router_outlet_selector || it?.moduleObject?.selector
                if (selctor) {
                    lastRouterSelector = selctor;
                }
            });
            console.log('===>', lastRouterSelector);
            this.render(executeModule, lastRouterSelector)
        } else {
            Renderer.render('404 not found')
        }
        // console.log('executeRouter-->', this.routers)
        // for (const router of this.routers) {
        //     if (router.hashchange([''])) {
        //         return router
        //     } else {
        //         Renderer.render('404 not found')
        //     }
        // }
    }

    public renderRouterModule(module: Module | undefined, targetSelector = 'app'): boolean {
        if (module && !module.exist()) {
            const renderStr = module.renderWrapString()
            module.onInit()
            Renderer.renderTo(targetSelector, renderStr)
            module.onChangedRendered()
            return true
        } else {
            return false
        }
    }

    public render(module: Module | undefined, targetSelector: string): boolean {
        if (module) {
            const renderStr = module.renderString()
            module.onInit()
            Renderer.renderTo(targetSelector, renderStr)
            module.onChangedRendered()
            return true
        } else {
            return false
        }
    }
    // public getSim<T>(key: ConstructorType<T>): T {
    //     return simstanceManager.getOrNewSim(key)
    // }
}
