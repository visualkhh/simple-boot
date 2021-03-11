import {Module} from '@src/com/simple/boot/module/Module'
import {Renderer} from '@src/com/simple/boot/render/Renderer'
import {ConstructorType} from '@src/com/simple/boot/types/Types'
import {SimstanceManager} from '@src/com/simple/boot/simstance/SimstanceManager'

export interface Routers {
    [name: string]: ConstructorType<any> | any
}

export abstract class Router implements Routers {
    constructor(public root: string, public childs: ConstructorType<Router>[] = []) {
    }

    hashchange(parentRoots: string): boolean {
        const path = window.location.hash.replace('#', '')
        if (this.isRootUrl(parentRoots, path)) {
            // 내가 그리지못하면 -> 없으면
            if (!this.render(this.routing(parentRoots, path))) {
                // 자식중에 그려라.
                for (const child of this.childs) {
                    const route = SimstanceManager.getSim(child)
                    if (route && route.hashchange(parentRoots + (this.root || ''))) {
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
        return hashUrl.startsWith(parentRoots + this.root)
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
        const urlRoot = parentRoots + this.root
        const regex = new RegExp('^' + urlRoot, 'i')
        path = path.replace(regex, '')
        const fieldModule = (routers[path] as ConstructorType<any>)
        console.log('routing path ', this.root, path, fieldModule)
        if (fieldModule) {
            return SimstanceManager.getSim(fieldModule)
        }
    }
}
