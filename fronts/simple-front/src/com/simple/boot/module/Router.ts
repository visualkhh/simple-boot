import {Module} from '@src/com/simple/boot/module/Module'
import {Renderer} from '@src/com/simple/boot/render/Renderer'
import {ConstructorType} from '@src/com/simple/boot/types/Types'
import {SimstanceManager} from '@src/com/simple/boot/simstance/SimstanceManager'

export interface Routers {
    [name: string]: ConstructorType<any> | any
}

export abstract class Router implements Routers {
    constructor(public root: string) {
    }

    hashchange(event?: HashChangeEvent) {
        // console.log('hashchange router', event)
        if (event) {
            console.log('-->', event.oldURL, event.newURL);
        }
        const path = window.location.hash.replace('#', '')
        const renderModule = this.routing(path)
        this.render(renderModule);
    }

    public render(module: Module | undefined): void {
        let renderStr = ''
        if (module) {
            renderStr = module.renderString()
            module.onInit()
        } else {
            renderStr = '404'
        }
        Renderer.render(renderStr)
        module?.onChangedRendered();
    }

    public routing(path: string): Module | undefined {
        const routers = this as Routers;
        // const regex = /^The/i;
        const regex = new RegExp('^' + this.root, 'i');
        path = path.replace(regex, '');
        const newVar = (routers[path] as ConstructorType<any>)
        return SimstanceManager.getSim(newVar);
    }

    // [name: string]: ConstructorType<any>
}
