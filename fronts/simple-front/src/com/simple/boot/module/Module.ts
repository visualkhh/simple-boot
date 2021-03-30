import Handlebars from 'handlebars'
import {Renderer} from '../../../../com/simple/boot/render/Renderer'
import {LifeCycle} from '../module/LifeCycle'
import {fromEvent} from "rxjs";
import {View} from "../service/view/View";
import {RandomUtils} from "../util/random/RandomUtils";

export class Module implements LifeCycle {
    public router_outlet_selector: string | undefined
    public styleImports: string[] | undefined
    public originalSelector: string;

    constructor(public selector = '', public template = '{{data}}', public wrapElement = 'div') {
        this.originalSelector = selector;
        this.selector = `___Module___${this.selector}_${RandomUtils.uuid()}`
        if (this.template.search('\\[router-outlet\\]')) {
            this.router_outlet_selector = `___Module___router-outlet_${this.selector}_${RandomUtils.uuid()}`
            this.template = this.template.replace('[router-outlet]', ` id='${this.router_outlet_selector}' `)
        }
    }

    public renderString(): string {
        return Handlebars.compile(this.template)(this)
    }

    private setEvent(endFix: string) {
        const selectors = 'module-event-' + endFix;
        document.querySelectorAll('[' + selectors + ']').forEach(it => {
            if (!it.id) {
                it.id = `___Module___${this.originalSelector}_child-element_${RandomUtils.uuid()}`
            }
            const attribute = it.getAttribute(selectors);
            const newVar = this as any;
            if (attribute && newVar[attribute]) {
                fromEvent<MouseEvent>(it, endFix).subscribe(it => {
                    const view = new View(it.target! as Element);
                    newVar[attribute](it, view);
                })
            }
        })

    }
    public privateInit() {
        // Renderer.renderTo(this.selector, '')
        this.onInit()
    }

    public privateChangedRendered() {
        this.setEvent('click');
        this.setEvent('change');
        this.setEvent('keyup');
        this.setEvent('keydown');
        this.onChangedRendered()
    }

    public privateInitedChiled() {
        this.onInitedChiled()
    }

    public privateFinish() {
        this.onFinish()
    }

    public onInit() {
    }

    public onChangedRendered() {
    }

    public onInitedChiled() {
    }

    public onFinish() {
    }

    public render(selector = this.selector || Renderer.selector) {
        Renderer.renderTo(selector, this)
        this.transStyle(this.selector);
    }

    public renderWrap(selector = this.selector || Renderer.selector) {
        Renderer.renderTo(selector, this.renderWrapString())
        this.privateChangedRendered();
        this.transStyle(this.selector);
    }

    public transStyle(selector: string): void {
        const join = this.styleImports?.map(it => {
            // eslint-disable-next-line prefer-regex-literals
            const regExp = new RegExp('\\/\\*\\[module\\-selector\\]\\*\\/', 'gi') // 생성자
            return it.replace(regExp, '#' + selector + ' ');
        }).join(' ');
        Renderer.prependStyle(selector, join);
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
        return Renderer.exist(this.selector)
    }

    public toString(): string {
        return this.renderWrapString()
    }
}
