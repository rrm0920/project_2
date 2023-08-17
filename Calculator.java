import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Objects;
import java.util.Stack;

import javax.swing.*;

//SX2216129芮茹梦
//Calculator类，继承JFrame框架，实现事件监听器接口
public class Calculator extends JFrame implements ActionListener {
    private final String[] KEYS = {"7", "8", "9", "AC", "4", "5", "6", "-", "1", "2", "3", "+", "0", "e", "pi", "/", "sqrt", "%", "x*x", "*", "(", ")", ".", "="};
    private JButton keys[] = new JButton[KEYS.length];
    private JTextArea resultText = new JTextArea("0.0");    // 文本域组件TextArea可容纳多行文本；文本框内容初始值设为0.0
    private JTextArea History = new JTextArea();    // 历史记录文本框初始值设为空
    private JPanel jp2 = new JPanel();
    private JScrollPane gdt1 = new JScrollPane(resultText);     //给输入显示屏文本域新建一个垂直滚动滑条
    private JScrollPane gdt2 = new JScrollPane(History);    //给历史记录文本域新建一个垂直滚动滑条
    private JLabel label = new JLabel("历史记录");
    private String input = "";   //计算文本框输入的中缀表达式

    // 构造方法
    public Calculator() {
        super("Caculator");//“超”关键字，表示调用父类的构造函数，
        resultText.setBounds(20, 18, 255, 115);     // 设置文本框大小
        resultText.setAlignmentX(RIGHT_ALIGNMENT);  // 文本框内容右对齐
        resultText.setEditable(false);  // 文本框不允许修改结果
        resultText.setFont(new Font("monospaced", Font.PLAIN, 18));    //设置结果文本框输入文字的字体、类型、大小
        History.setFont(new Font("monospaced", Font.PLAIN, 18));    //设置历史记录文本框输入文字的字体、类型、大小
        History.setBounds(290, 40, 250, 370);   // 设置文本框大小
        History.setAlignmentX(LEFT_ALIGNMENT);  // 文本框内容右对齐
        History.setEditable(false);     // 文本框不允许修改结果
        label.setBounds(300, 15, 100, 20);  //设置标签位置及大小
        jp2.setBounds(290, 40, 250, 370);   //设置面板窗口位置及大小
        jp2.setLayout(new GridLayout());
        JPanel jp1 = new JPanel();
        jp1.setBounds(20, 18, 255, 115);    //设置面板窗口位置及大小
        jp1.setLayout(new GridLayout());
        resultText.setLineWrap(true);   // 激活自动换行功能
        resultText.setWrapStyleWord(true);  // 激活断行不断字功能
        resultText.setSelectedTextColor(Color.RED);
        History.setLineWrap(true);  //自动换行
        History.setWrapStyleWord(true);
        History.setSelectedTextColor(Color.blue);
        gdt1.setViewportView(resultText);   //使滚动条显示出来
        gdt2.setViewportView(History);
        gdt1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);     //设置让垂直滚动条一直显示
        gdt2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);     //设置让垂直滚动条一直显示
        gdt2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);     //设置让水平滚动条一直显示
        jp1.add(gdt1);  //将滚动条添加入面板窗口中
        jp2.add(gdt2);
        this.add(jp1);  //将面板添加到总窗体中
        this.add(jp2);  //将面板添加到总窗体中
        this.setLayout(null);
        this.add(label);    // 新建“历史记录”标签

        // 放置按钮 x,y为按钮的横纵坐标
        int x = 20, y = 150;
        for (int i = 0; i < KEYS.length; i++) {
            keys[i] = new JButton();
            keys[i].setText(KEYS[i]);
            keys[i].setBounds(x, y, 60, 40);
            if (x < 215) {
                x += 65;
            } else {
                x = 20;
                y += 45;
            }
            this.add(keys[i]);
        }
        for (int i = 0; i < KEYS.length; i++)   // 每个按钮都注册事件监听器
        {
            keys[i].addActionListener(this);
        }
        this.setResizable(false);
        this.setBounds(500, 200, 567, 480);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    // 事件处理
    public void actionPerformed(ActionEvent e) {
        String label = e.getActionCommand();    //获得事件源的标签
        if (Objects.equals(label, "AC"))    //清空按钮，消除显示屏文本框前面所有的输入和结果
        {
            input = "";
            resultText.setText("0.0");      //更新文本域的显示，显示初始值;
        } else if (Objects.equals(label, "sqrt")) {
            String n;
            if(input.isEmpty()) n="error!";     //加判断，先输入数字再输入开方符号才是合法的
            else n = String.valueOf(kfys(input));
            resultText.setText("sqrt" + "(" + input + ")" + "=" + n);       //使运算表达式显示在输入界面
            History.setText(History.getText() + resultText.getText() + "\n");       //获取输入界面的运算表达式并使其显示在历史记录文本框
            input = n;
        } else if (Objects.equals(label, "x*x")) {
            String m;
            if(input.isEmpty()) m="error!";
            else m = String.valueOf(pfys(input));
            resultText.setText(input + "^2" + "=" + m);
            History.setText(History.getText() + resultText.getText() + "\n");
            input = m;
        } else if (Objects.equals(label, "=")) {
            if (input.isEmpty()) return;
            String[] s = houzhui(input);    //将中缀表达式转换为后缀表达式
            double result = Result(s);     //计算后缀表达式得出最终算式结果
            resultText.setText(input + "=" + result);
            History.setText(History.getText() + resultText.getText() + "\n");
        } else {
            if (Objects.equals(label, "e")) {
                String m = String.valueOf(2.71828);     //将e的值以字符串的形式传给m
                label = m;
            } else if (Objects.equals(label, "pi")) {
                String m = String.valueOf(3.14159);
                label = m;
            }
            input = input + label;
            resultText.setText(input);
        }
    }

    //将中缀表达式转换为后缀表达式
    private String[] houzhui(String infix) {  //infix 中缀
        String s = "";// 用于承接多位数的字符串
        Stack<String> opStack = new Stack<String>();    // 操作符静态栈,对用户输入的操作符进行处理，用于存储运算符
        Stack<String> postQueue = new Stack<String>();   // 后缀表达式，为了将多位数存储为独立的字符串
        System.out.println("中缀：" + infix);
        for (int i = 0; i < infix.length(); i++)    // 遍历中缀表达式
        // indexof函数，返回字串首次出现的位置；charAt函数返回index位置处的字符；
        {
            if ("1234567890.".indexOf(infix.charAt(i)) >= 0) {    // 遇到数字字符直接入队
                //判断并记录多位操作数，比如，中缀表达式：234+4*2，我们扫描这个字符串的时候，s的作用相当于用来存储长度为3个字符的操作数：234
                s = "";// 作为承接字符，每次开始时都要清空
                for (; i < infix.length() && "0123456789.".indexOf(infix.charAt(i)) >= 0; i++) {
                    s = s + infix.charAt(i);
                }
                i--;   //避免跳过对非数字字符的处理
                postQueue.push(s);  // 数字字符直接加入后缀表达式
            } else if ("(".indexOf(infix.charAt(i)) >= 0) {   // 遇到左括号
                opStack.push(String.valueOf(infix.charAt(i)));   // 左括号入栈
            } else if (")".indexOf(infix.charAt(i)) >= 0) {   // 遇到右括号
                while (!opStack.peek().equals("(")) {    // 栈顶元素循环出栈，直到遇到左括号为止
                    postQueue.push(opStack.pop());
                }
                opStack.pop();     //删除左括号
            } else if ("*%/+-".indexOf(infix.charAt(i)) >= 0)   // 遇到运算符
            {
                if (opStack.empty() || "(".contains(opStack.peek())) {   // 若栈为空或栈顶元素为左括号则直接入栈
                    opStack.push(String.valueOf(infix.charAt(i)));
                } else {
                    // 当栈顶元素为高优先级或同级运算符时,让栈顶元素出栈进入后缀表达式后,直到符合规则后，当前运算符再入栈
                    boolean rule = ("*%/+-".contains(opStack.peek()) && "+-".indexOf(infix.charAt(i)) >= 0) || ("*%/".contains(opStack.peek()) && "*%/".indexOf(infix.charAt(i)) >= 0);
                    while (!opStack.empty() && rule) {
                        postQueue.push(opStack.peek());  //peek()方法：返回栈顶的元素但不移除它
                        opStack.pop();
                    }
                    opStack.push(String.valueOf(infix.charAt(i)));   // 当前元素入栈
                }
            }
        }
        while (!opStack.empty()) {// 遍历结束后将栈中剩余元素依次出栈进入后缀表达式
            postQueue.push(opStack.pop());
        }
        //将后缀表达式栈转换为字符串数组格式
        String[] suffix = new String[postQueue.size()];
        for (int i = postQueue.size() - 1; i >= 0; i--) {
            suffix[i] = postQueue.pop();
        }
        System.out.println("后缀：" + Arrays.toString(suffix.clone()));
        return suffix;
    }

    //开方运算方法
    public double kfys(String str) {
        double a = Double.parseDouble(str);
        return Math.sqrt(a);
    }

    //平方运算方法
    public double pfys(String str) {
        double a = Double.parseDouble(str);
        return Math.pow(a, 2);
    }

    // 计算后缀表达式，并返回最终结果
    public double Result(String[] suffix) {  //suffix 后缀
        Stack<String> Result = new Stack<>();// 顺序存储的栈，数据类型为字符串
        int i;
        for (i = 0; i < suffix.length; i++) {
            if ("1234567890.".indexOf(suffix[i].charAt(0)) >= 0) {  //遇到数字，直接入栈
                Result.push(suffix[i]);
            } else {    // 遇到运算符字符，将栈顶两个元素出栈计算并将结果返回栈顶
                double x, y, n = 0;
                x = Double.parseDouble(Result.pop());   // 顺序出栈两个数字字符串，并转换为double类型
                y = Double.parseDouble(Result.pop());
                switch (suffix[i]) {
                    case "*":
                        n = y * x;
                        break;
                    case "/":
                        if (x == 0) return 000000;
                        else n = y / x;
                        break;
                    case "%":
                        if (x == 0) return 000000;
                        else n = y % x;
                        break;
                    case "-":
                        n = y - x;
                        break;
                    case "+":
                        n = y + x;
                        break;
                }
                Result.push(String.valueOf(n)); // 将运算结果重新入栈
            }
        }

        System.out.println("return:" + Result.peek());
        return Double.parseDouble(Result.peek());   // 返回最终结果
    }

    // 主函数
    public static void main(String[] args) {
        Calculator a = new Calculator();
    }
}


