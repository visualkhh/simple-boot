import {Module} from '@src/com/simple/boot/module/Module'
import {Renderer} from '@src/com/simple/boot/render/Renderer'
import {ConstructorType} from '@src/com/simple/boot/types/Types'
import {simstanceManager} from '@src/com/simple/boot/simstance/SimstanceManager'
import {LocationUtils} from '@src/com/simple/boot/util/window/LocationUtils'

export interface Routers {
    [name: string]: ConstructorType<any> | any
}

export class Router implements Routers {
    // constructor(public path: string, public module = new Module('router-empty'), public childs: ConstructorType<Router>[] = []) {
    constructor(public path: string, public module?: ConstructorType<Module>, public childs: ConstructorType<Router>[] = []) {
    }

    hashchange(parentRoots: string[]): boolean {
        // console.log('---routing hashChange -> ', parentRoots);
        const path = LocationUtils.hash();
        if (this.isRootUrl(parentRoots, path)) {
            // 내가 그리지못하면 -> 없으면
            const fieldModule = this.routing(parentRoots, path)
            if (this.moduleObject && fieldModule) {
                this.renderRouterModule(this.moduleObject);
            }
            if (!this.render(fieldModule, (this.moduleObject?.router_outlet_selector || this.moduleObject?.selector))) {
                // 내꺼에 없으면 자식중에 그려라.
                for (const child of this.childs) {
                    const route = simstanceManager.getOrNewSim(child)
                    const childParentRoot = parentRoots.slice();
                    childParentRoot.push((this.path || ''))
                    if (route && route.hashchange(childParentRoot)) {
                        return true
                    }
                }
                return false
            } else { // 마지막 true
                return true
            }
        } else {
            return false
        }
    }

    get moduleObject() {
        if (this.module) {
            return simstanceManager.getOrNewSim(this.module)
        }
    }

    public isRootUrl(parentRoots: string[], hashUrl: string): boolean {
        return hashUrl.startsWith(parentRoots.join('') + (this.path || ''))
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

    public render(module: Module | undefined, targetSelector = 'app'): boolean {
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


    // my field find
    public routing(parentRoots: string[], path: string): Module | undefined {
        console.log('--routing-->', path)
        const routers = this as Routers
        const urlRoot = parentRoots.join('') + this.path
        const regex = new RegExp('^' + urlRoot, 'i')
        path = path.replace(regex, '')
        const fieldModule = (routers[path] as ConstructorType<any>)
        console.log('routing path ', this.path, path, fieldModule)
        if (fieldModule) {
            return simstanceManager.getOrNewSim(fieldModule)
        }
    }
}
