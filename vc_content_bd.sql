PGDMP  &                    }         
   vc-content    17.2    17.2 "    �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                           false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                           false            �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                           false            �           1262    55728 
   vc-content    DATABASE     �   CREATE DATABASE "vc-content" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Kazakh_Kazakhstan.utf8';
    DROP DATABASE "vc-content";
                     postgres    false            �            1259    55730    content_types    TABLE     �   CREATE TABLE public.content_types (
    type_id integer NOT NULL,
    type_name character varying(50) NOT NULL,
    type_icon character varying(100)
);
 !   DROP TABLE public.content_types;
       public         heap r       postgres    false            �            1259    55729    content_types_type_id_seq    SEQUENCE     �   CREATE SEQUENCE public.content_types_type_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 0   DROP SEQUENCE public.content_types_type_id_seq;
       public               postgres    false    218            �           0    0    content_types_type_id_seq    SEQUENCE OWNED BY     W   ALTER SEQUENCE public.content_types_type_id_seq OWNED BY public.content_types.type_id;
          public               postgres    false    217            �            1259    55739    media_items    TABLE     g  CREATE TABLE public.media_items (
    item_id integer NOT NULL,
    type_id integer,
    title character varying(255) NOT NULL,
    creator character varying(255),
    description text,
    thumbnail_url character varying(255),
    external_url character varying(255),
    release_date date,
    added_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP
);
    DROP TABLE public.media_items;
       public         heap r       postgres    false            �            1259    55738    media_items_item_id_seq    SEQUENCE     �   CREATE SEQUENCE public.media_items_item_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 .   DROP SEQUENCE public.media_items_item_id_seq;
       public               postgres    false    220            �           0    0    media_items_item_id_seq    SEQUENCE OWNED BY     S   ALTER SEQUENCE public.media_items_item_id_seq OWNED BY public.media_items.item_id;
          public               postgres    false    219            �            1259    55763 
   media_tags    TABLE     _   CREATE TABLE public.media_tags (
    media_id integer NOT NULL,
    tag_id integer NOT NULL
);
    DROP TABLE public.media_tags;
       public         heap r       postgres    false            �            1259    55755    tags    TABLE     g   CREATE TABLE public.tags (
    tag_id integer NOT NULL,
    tag_name character varying(50) NOT NULL
);
    DROP TABLE public.tags;
       public         heap r       postgres    false            �            1259    55754    tags_tag_id_seq    SEQUENCE     �   CREATE SEQUENCE public.tags_tag_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 &   DROP SEQUENCE public.tags_tag_id_seq;
       public               postgres    false    222            �           0    0    tags_tag_id_seq    SEQUENCE OWNED BY     C   ALTER SEQUENCE public.tags_tag_id_seq OWNED BY public.tags.tag_id;
          public               postgres    false    221            /           2604    55733    content_types type_id    DEFAULT     ~   ALTER TABLE ONLY public.content_types ALTER COLUMN type_id SET DEFAULT nextval('public.content_types_type_id_seq'::regclass);
 D   ALTER TABLE public.content_types ALTER COLUMN type_id DROP DEFAULT;
       public               postgres    false    218    217    218            0           2604    55742    media_items item_id    DEFAULT     z   ALTER TABLE ONLY public.media_items ALTER COLUMN item_id SET DEFAULT nextval('public.media_items_item_id_seq'::regclass);
 B   ALTER TABLE public.media_items ALTER COLUMN item_id DROP DEFAULT;
       public               postgres    false    219    220    220            2           2604    55758    tags tag_id    DEFAULT     j   ALTER TABLE ONLY public.tags ALTER COLUMN tag_id SET DEFAULT nextval('public.tags_tag_id_seq'::regclass);
 :   ALTER TABLE public.tags ALTER COLUMN tag_id DROP DEFAULT;
       public               postgres    false    221    222    222            �          0    55730    content_types 
   TABLE DATA           F   COPY public.content_types (type_id, type_name, type_icon) FROM stdin;
    public               postgres    false    218   �'       �          0    55739    media_items 
   TABLE DATA           �   COPY public.media_items (item_id, type_id, title, creator, description, thumbnail_url, external_url, release_date, added_at) FROM stdin;
    public               postgres    false    220   (       �          0    55763 
   media_tags 
   TABLE DATA           6   COPY public.media_tags (media_id, tag_id) FROM stdin;
    public               postgres    false    223   �-       �          0    55755    tags 
   TABLE DATA           0   COPY public.tags (tag_id, tag_name) FROM stdin;
    public               postgres    false    222   �-       �           0    0    content_types_type_id_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('public.content_types_type_id_seq', 5, true);
          public               postgres    false    217            �           0    0    media_items_item_id_seq    SEQUENCE SET     E   SELECT pg_catalog.setval('public.media_items_item_id_seq', 8, true);
          public               postgres    false    219            �           0    0    tags_tag_id_seq    SEQUENCE SET     =   SELECT pg_catalog.setval('public.tags_tag_id_seq', 1, true);
          public               postgres    false    221            4           2606    55735     content_types content_types_pkey 
   CONSTRAINT     c   ALTER TABLE ONLY public.content_types
    ADD CONSTRAINT content_types_pkey PRIMARY KEY (type_id);
 J   ALTER TABLE ONLY public.content_types DROP CONSTRAINT content_types_pkey;
       public                 postgres    false    218            6           2606    55737 )   content_types content_types_type_name_key 
   CONSTRAINT     i   ALTER TABLE ONLY public.content_types
    ADD CONSTRAINT content_types_type_name_key UNIQUE (type_name);
 S   ALTER TABLE ONLY public.content_types DROP CONSTRAINT content_types_type_name_key;
       public                 postgres    false    218            9           2606    55747    media_items media_items_pkey 
   CONSTRAINT     _   ALTER TABLE ONLY public.media_items
    ADD CONSTRAINT media_items_pkey PRIMARY KEY (item_id);
 F   ALTER TABLE ONLY public.media_items DROP CONSTRAINT media_items_pkey;
       public                 postgres    false    220            ?           2606    55767    media_tags media_tags_pkey 
   CONSTRAINT     f   ALTER TABLE ONLY public.media_tags
    ADD CONSTRAINT media_tags_pkey PRIMARY KEY (media_id, tag_id);
 D   ALTER TABLE ONLY public.media_tags DROP CONSTRAINT media_tags_pkey;
       public                 postgres    false    223    223            ;           2606    55760    tags tags_pkey 
   CONSTRAINT     P   ALTER TABLE ONLY public.tags
    ADD CONSTRAINT tags_pkey PRIMARY KEY (tag_id);
 8   ALTER TABLE ONLY public.tags DROP CONSTRAINT tags_pkey;
       public                 postgres    false    222            =           2606    55762    tags tags_tag_name_key 
   CONSTRAINT     U   ALTER TABLE ONLY public.tags
    ADD CONSTRAINT tags_tag_name_key UNIQUE (tag_name);
 @   ALTER TABLE ONLY public.tags DROP CONSTRAINT tags_tag_name_key;
       public                 postgres    false    222            7           1259    55753    idx_media_items_type_id    INDEX     R   CREATE INDEX idx_media_items_type_id ON public.media_items USING btree (type_id);
 +   DROP INDEX public.idx_media_items_type_id;
       public                 postgres    false    220            @           2606    55748 $   media_items media_items_type_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.media_items
    ADD CONSTRAINT media_items_type_id_fkey FOREIGN KEY (type_id) REFERENCES public.content_types(type_id);
 N   ALTER TABLE ONLY public.media_items DROP CONSTRAINT media_items_type_id_fkey;
       public               postgres    false    4660    218    220            A           2606    55768 #   media_tags media_tags_media_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.media_tags
    ADD CONSTRAINT media_tags_media_id_fkey FOREIGN KEY (media_id) REFERENCES public.media_items(item_id) ON DELETE CASCADE;
 M   ALTER TABLE ONLY public.media_tags DROP CONSTRAINT media_tags_media_id_fkey;
       public               postgres    false    4665    220    223            B           2606    55773 !   media_tags media_tags_tag_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.media_tags
    ADD CONSTRAINT media_tags_tag_id_fkey FOREIGN KEY (tag_id) REFERENCES public.tags(tag_id) ON DELETE CASCADE;
 K   ALTER TABLE ONLY public.media_tags DROP CONSTRAINT media_tags_tag_id_fkey;
       public               postgres    false    222    4667    223            �   M   x�3���/�L���\F�a�)��
鉹��e &��e�阗	I�\&���y鉜� �˔ӷ�83�3Dr��qqq kP�      �   k  x��VMs�6=ӿ�K۩H��(˺%N�f;���\2�IHD ������e��ЋM� ����vQ$E�q���M�:+�7C',{0��˯L:�Y��S&ٰZ���C'�Xky��J�����^�	�i�F��u�ZiE�E��={'K�o	���7iY�c��UZܲ�\�\d��=��ʤL^�J1O޵_�����d1�|:�f�3�#ښ���b+�"ٲ�	�0�J�.~|%|'\��c7.cu"�P�㰞�0�H;ϕ��x�*'��9�t��?LC� ڊ�Y�S-�M'�Ȉ�@N1�LO���e>]VEV��r�����c~f��mVUEU,��
�<�@_<-ٽį\C���Z�e��z�\|b+ӌ8�0o����F�uj ?����A����7 w������V{Ҋ�Y#m3J�[�ow�wnV2l�������i6���L3��$@�^�%�-�|C?M<�%�~� ʾ��G$ژ~�^s�+�#ᄀbBI��<q�����EX���89��f2�.B�h�s��G�J�.�A�����h�� 0{c6�NZ�p{h4Q�Hq%�H�e�Q�!��z�bk���4{��L�k&{�܊�T9޶�*)l)3O�_��ŋ��0�lT'ם��T���SR���`�h�!cw�z��8�:�OPw{�l�Z\$�k�H*+�3ZI�֏�˨ڊ�gː_����X��kn�b6-+r�����U�A�`��o����ͱ����U���p �3��+�A�q�(��R�f�����\KO��z6�[p9a_��}�>�M�%|���3�_����o�$�@XK5��R Ԣ���ѿ���E(H��:�O�c��?*R ���R�?e	����R���!߭�<Ŧ�Q�w�qi��Ek�z=�1�Q��alHoaIHw����:�s:�é�¥J�,��bb��]H ��(�C_��~�Bc�@ƅzf��'wN�z�X�nJ����{޲{�ϒ�ѥ>�&ca�{XW��N��b/њ,0pp������ұ<�!<v�p�hLz�}��xyv�x:r��xM�;���E��授ڇSdc��;3�
s쪮��\4r�6N��aw��<��=D�����,�B;�	s�d2a���ě��c�As"D t�֢��?�t���vpHr:��1 zb;l�ү�j��t�#��>��R� ����-����vr#av�3p}M����w��w��� *o�ڏZP�/��eQe�jZ�S�D�2����-zI��fp8��9����=���n�����Z\��$	-^2&?G,:�l���-�^P�"���4�H��%﹄8�*�����4/�|��*'Lo�:�[vuu�/��!      �      x�3�4����� ]      �      x�3�t)J�M����� ��     