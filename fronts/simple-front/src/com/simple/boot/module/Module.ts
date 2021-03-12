import Handlebars from 'handlebars'
import {Renderer} from '@src/com/simple/boot/render/Renderer'
const {v4: uuidv4} = require('uuid')
export class Module {
    // constructor(public selector?: string | undefined, public template?: string | undefined) {
    constructor(public selector = '', public template = '', public wrapElement = 'div') {
        this.selector = `___Module___${this.selector}_${uuidv4()}`
        console.log('module constructor, ', 'isProxy' in this)
    }

    public renderString(): string {
        return Handlebars.compile(this.template)(this)
    }

    public onInit() {
    }

    public onChangedRendered() {
    }

    public render() {
        Renderer.renderTo(this.selector || Renderer.selector, this)
    }

    public renderWrapString(): string {
        return `<${this.wrapElement} id="${this.selector}">${Handlebars.compile(this.template)(this)}</${this.wrapElement}>`
        // if (!this.selector) {
        //     return Handlebars.compile(this.template)(this);
        // } else if (this.selector.startsWith('.')) {
        //     return `<${this.wrapElement} class="${this.selector.replace('.', ' ')}">${Handlebars.compile(this.template)(this)}</${this.wrapElement}>`;
        // } else if (this.selector.startsWith('#')) {
        //     return `<${this.wrapElement} id="${this.selector.replace('#', '')}">${Handlebars.compile(this.template)(this)}</${this.wrapElement}>`;
        // } else {
        //     return `<${this.wrapElement} id="${this.selector.replace('#', '')}">${Handlebars.compile(this.template)(this)}</${this.wrapElement}>`;
        // }
    }

    public toString(): string {
        return this.renderWrapString()
    }
}
