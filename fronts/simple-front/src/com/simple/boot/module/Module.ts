import Handlebars from 'handlebars'
import {Renderer} from '@src/com/simple/boot/render/Renderer'

export class Module {
    constructor(public selector?: string | undefined) {
    }

    public renderString(): string {
        return Handlebars.compile(this.template)(this);
    }

    public onInit() {}
    public onChangeRendered() {}

    get template(): string {
        return ''
    }

    public render() {
        Renderer.renderTo(this.selector || Renderer.selector, this);
    }
}
