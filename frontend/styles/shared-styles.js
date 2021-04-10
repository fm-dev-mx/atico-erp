// eagerly import theme styles so as we can override them
import '@vaadin/vaadin-lumo-styles/all-imports';

const $_documentContainer = document.createElement('template');

$_documentContainer.innerHTML = `
<custom-style>
  <style>
    html {
      --lumo-body-text-color: hsla(214, 12%, 42%, 0.94);
    }

    [theme~="dark"] {
      --lumo-primary-color: rgb(10, 11, 10);
      --lumo-primary-text-color: rgb(250, 255, 255);
      --lumo-base-color: hsl(214, 0%, 97%);
      --lumo-body-text-color: hsla(214, 100%, 99%, 0.9);
    }
  </style>
</custom-style>

<dom-module id="button-style" theme-for="vaadin-button">
  <template>
    <style>
      :host(:not([theme~="tertiary"])) {
        background-image: linear-gradient(var(--lumo-tint-5pct), var(--lumo-shade-5pct));
        box-shadow: inset 0 0 0 1px var(--lumo-contrast-20pct);
      }

      :host(:not([theme~="tertiary"]):not([theme~="primary"]):not([theme~="error"]):not([theme~="success"])) {
        color: var(--lumo-body-text-color);
      }

      :host([theme~="primary"]) {
        text-shadow: 0 -1px 0 var(--lumo-shade-20pct);
      }
    </style>
  </template>
</dom-module>


`;

document.head.appendChild($_documentContainer.content);
