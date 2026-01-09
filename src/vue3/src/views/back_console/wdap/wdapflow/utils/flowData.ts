export interface FlowLink {
  source: string;
  target: string;
  label?: string;
  lineStyle?: {
    color?: string;
    shadowColor?: string;
    shadowBlur?: number;
  };
}

export interface FlowNode {
  name: string;
  subTitle?: string;
  x?: number;
  y?: number;
  itemStyle?: {
    color?: string;
    borderColor?: string;
    borderWidth?: number;
  };
}

export interface FlowData {
  links: FlowLink[];
  nodes: FlowNode[];
}
