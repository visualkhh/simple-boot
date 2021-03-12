import {Module} from '@src/com/simple/boot/module/Module'
import {Renderer} from '@src/com/simple/boot/render/Renderer'
import {ConstructorType} from '@src/com/simple/boot/types/Types'
import {simstanceManager} from '@src/com/simple/boot/simstance/SimstanceManager'
import {LocationUtils} from '@src/com/simple/boot/util/window/LocationUtils'

export interface Routers {
    [name: string]: ConstructorType<any> | any
}

export class Router implements Routers {
    constructor(public path: string, public module = new Module('empty'), public childs: ConstructorType<Router>[] = []) {
    }

    getExecutorRouter(parentRoots: string): Router | undefined {
        const path = LocationUtils.hash();
        if (this.isRootUrl(parentRoots, path)) {
            // 내가 가지고 있지 않으면
            if (!this.routing(parentRoots, path)) {
                // 자식중에 찾아라..
                for (const child of this.childs) {
                    const route = simstanceManager.getOrNewSim(child)
                    const executorRouter = route.getExecutorRouter(parentRoots + (this.path || ''))
                    if (route && executorRouter) {
                        return executorRouter
                    }
                }
            }
            return this
        }
    }

    hashchange(parentRoots: string): boolean {
        const path = LocationUtils.hash();
        if (this.isRootUrl(parentRoots, path)) {
            // 내가 그리지못하면 -> 없으면
            if (!this.render(this.routing(parentRoots, path))) {
                // 자식중에 그려라.
                for (const child of this.childs) {
                    const route = simstanceManager.getOrNewSim(child)
                    if (route && route.hashchange(parentRoots + (this.path || ''))) {
                        return true
                    }
                }
                return false
            }
            return true
        } else {
            return false
        }
    }

    public isRootUrl(parentRoots: string, hashUrl: string): boolean {
        return hashUrl.startsWith(parentRoots + this.path)
    }

    public render(module: Module | undefined): boolean {
        if (module) {
            const renderStr = module.renderString()
            module.onInit()
            Renderer.render(renderStr)
            module.onChangedRendered()
            return true
        } else {
            return false
        }
    }

    public routing(parentRoots: string, path: string): Module | undefined {
        console.log('--routing-->', path)
        const routers = this as Routers
        const urlRoot = parentRoots + this.path
        const regex = new RegExp('^' + urlRoot, 'i')
        path = path.replace(regex, '')
        const fieldModule = (routers[path] as ConstructorType<any>)
        console.log('routing path ', this.path, path, fieldModule)
        if (fieldModule) {
            return simstanceManager.getOrNewSim(fieldModule)
        }
    }
}
