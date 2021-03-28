import Handlebars from 'handlebars'
import {Renderer} from '@src/com/simple/boot/render/Renderer'
import {ConstructorType} from '@src/com/simple/boot/types/Types'
import {simstanceManager} from '@src/com/simple/boot/simstance/SimstanceManager'
import {Routers} from '@src/com/simple/boot/module/Router'
const {v4: uuidv4} = require('uuid')
export class Module {
    // constructor(public selector?: string | undefined, public template?: string | undefined) {
    public router_outlet_selector: string | undefined;
    public styleImports: any[] | undefined;
    constructor(public selector = '', public template = '', public wrapElement = 'div') {
        this.selector = `___Module___${this.selector}_${uuidv4()}`
        if (this.template.search('\\[router-outlet\\]')) {
            this.router_outlet_selector = `___Module___router-outlet_${this.selector}_${uuidv4()}`
            this.template = this.template.replace('[router-outlet]', ` id='${this.router_outlet_selector}' `)
        }
        console.log('module constructor, ', 'selector', selector, 'isProxy' in this)
    }

    public renderString(): string {
        return Handlebars.compile(this.template)(this)
    }

    public onInit() {
    }

    public onChangedRendered() {
    }

    public render(selector = this.selector || Renderer.selector) {
        Renderer.renderTo(selector, this)
        console.log('-->', this.transStyle(selector));
        Renderer.prependStyle(selector, this.transStyle(selector));
    }

    public renderWrap(selector = this.selector || Renderer.selector) {
        Renderer.renderTo(selector, this.renderWrapString())
        console.log('-->', this.transStyle(selector));
        Renderer.prependStyle(selector, this.transStyle(selector));
    }

    public transStyle(selector: string): string | undefined {
        return this.styleImports?.map(it => it[0][1]).map((it: string) => {
            // eslint-disable-next-line prefer-regex-literals
            const regExp = new RegExp('\\/\\*\\[module\\-selector\\]\\*\\/', 'gi') // 생성자
            return it.replace(regExp, '#' + selector + ' ')
        }).join(' ');
    }
    // public renderWrap() {
    //     Renderer.renderTo(this.selector || Renderer.selector, this)
    // }

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

    public exist(): boolean {
        return Renderer.exist(this.selector);
    }

    public toString(): string {
        return this.renderWrapString()
    }
}
