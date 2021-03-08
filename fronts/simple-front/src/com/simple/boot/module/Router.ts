import {Module} from '@src/com/simple/boot/module/Module'

export abstract class Router {
    hashchange(event?: HashChangeEvent) {
        console.log('hashchange router', event)
        const path = window.location.hash.replace('#', '')
        const renderModule = this.routing(path)
        this.render(renderModule);
    }

    public render(module: Module | undefined): void {
        let renderStr = ''
        if (module) {
            renderStr = module.render()
            module.onInit()
        } else {
            renderStr = '404'
        }
        document.querySelector('#app')!.innerHTML = renderStr
        module?.onChangeRendered();
    }

    public abstract routing(path: string): Module | undefined;
}
