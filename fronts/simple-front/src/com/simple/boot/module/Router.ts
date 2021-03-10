import {Module} from '@src/com/simple/boot/module/Module'
import {Renderer} from '@src/com/simple/boot/render/Renderer'

export abstract class Router {
    hashchange(event?: HashChangeEvent) {
        // console.log('hashchange router', event)
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
        module?.onChangeRendered();
    }

    public abstract routing(path: string): Module | undefined;
}
